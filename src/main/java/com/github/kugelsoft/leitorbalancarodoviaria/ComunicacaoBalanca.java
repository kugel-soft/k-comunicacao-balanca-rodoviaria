package com.github.kugelsoft.leitorbalancarodoviaria;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public abstract class ComunicacaoBalanca {

	private static final Logger logger = Logger.getLogger(ComunicacaoBalanca.class.getName());

	private static final int BYTES_BUFFER_SIZE = 10240;
	private static final byte[] EMPTY_ARRAY = new byte[0];
	protected ParametrosBalanca parametros;

	protected ComunicacaoBalanca(ParametrosBalanca parametros) {
		this.parametros = parametros;
		logger.finest("Iniciando " + getClass().getSimpleName() + " - " + parametros.getIp() + ":" + parametros.getPorta());
	}

	protected String substring(String retorno, int indexIniInclusive, int indexFimExclusive) {
		if (retorno == null || indexIniInclusive >= retorno.length()) {
			return "";
		}
		if (indexFimExclusive > retorno.length()) {
			indexFimExclusive = retorno.length();
		}
		return retorno.substring(indexIniInclusive, indexFimExclusive);
	}

	protected String enviarComando(String cmd) throws IOException {
		return enviarComando(cmd, 0);
	}

	protected String enviarComando(String cmd, int minChars) throws IOException {
		String retorno = "";
		Socket socket = null;
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(parametros.getIp(), parametros.getPorta()), parametros.getMilissegundosTimeoutComunicacao());
			socket.setSoTimeout(parametros.getMilissegundosTimeoutComunicacao());

			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			if (cmd != null) {
				outputStream.write(cmd.getBytes());
				outputStream.flush();
				logger.fine("Enviou: " + cmd);

				int tentativas = 0;
				do {
					if (tentativas > 0) {
						try {
							Thread.sleep(10);
						} catch (Exception ex) {
						}
					}
					byte[] bytes = new byte[BYTES_BUFFER_SIZE];
					int readBytes = inputStream.read(bytes);
					if (tentativas == 0 && readBytes > 0 && isIgnorarPrimeirosBytes()) {
						logger.finest("Ignorando " + readBytes + " bytes");
						outputStream.write(cmd.getBytes());
						outputStream.flush();
						readBytes = inputStream.read(bytes);
					}
					if (readBytes >= 0) {
						bytes = Arrays.copyOf(bytes, readBytes);
					} else {
						bytes = EMPTY_ARRAY;
					}
					String retornoLido = new String(bytes);
					retorno += retornoLido;
					logger.finest("Recebeu: [" + retornoLido.replace("\r", "\\r").replace("\n", "\\n") + "] bytes: " + Arrays.toString(bytes));
					tentativas++;
				} while (retorno.length() < minChars && tentativas < 300);

				logger.fine("Retorno: [" + retorno.replace("\r", "\\r").replace("\n", "\\n") + "]");

				String[] vals = retorno.split("[\r|\n]");
				if (vals.length > 1) {
					int qtdComLength = 0;
					int maxLength = 0;
					String novoRetorno = retorno;
					for (String val : vals) {
						if (maxLength == 0 || val.length() >= maxLength) {
							if (!val.isEmpty()) {
								qtdComLength++;
							}
							maxLength = val.length();
							novoRetorno = val;
						}
					}
					if (qtdComLength > 1) {
						retorno = novoRetorno;
						logger.fine("Considerando: " + retorno);
					}
				}
			}

			socket.close();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return retorno;
	}

	protected String rightTrim(String str) {
		int len = str.length();
		while (len > 0 && str.charAt(len - 1) <= ' ') {
			len--;
		}
		return ((len < str.length())) ? str.substring(0, len) : str;
	}

	protected abstract BigDecimal lerPesoModelo() throws IOException, PesoInvalidoException, PesoInstavelException;

	/**
	 * Faz a leitura do peso da balança
	 * @return BigDecimal com o peso
	 * @throws IOException Se ocorrer algum erro de comunicação com a balança
	 * @throws PesoInvalidoException Caso o peso esteja inválido
	 * @throws PesoInstavelException Caso o peso da balança não esteja estável
	 */
	public BigDecimal lerPeso() throws IOException, PesoInvalidoException, PesoInstavelException {
		BigDecimal peso = lerPesoModelo();

		int cont = parametros.getQuantidadeLeiturasConsiderarPesoEstavel();
		while (cont > 1) {
			try {
				Thread.sleep(parametros.getMilissegundosEntreLeiturasConsiderarPesoEstavel());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			BigDecimal novoPeso = lerPesoModelo();
			BigDecimal difQuantPeso = peso.subtract(novoPeso).abs();
			if (difQuantPeso.doubleValue() > parametros.getPesoToleranciaConsiderarPesoEstavel()) {
				throw new PesoInstavelException();
			}
			cont--;
		}
		return peso;
	}

	/**
	 * Testa a conexão com a balança e gera uma IOException se ocorrer algum erro de comunicação
	 * @throws IOException se ocorrer algum erro de comunicação
	 */
	public abstract void testarConexao() throws IOException;

	public boolean isIgnorarPrimeirosBytes() {
		return true;
	}

}
