package com.github.kugelsoft.leitorbalancarodoviaria;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public abstract class ComunicacaoBalanca {

	private static final int BYTES_BUFFER_SIZE = 10240;
	private static final byte[] EMPTY_ARRAY = new byte[0];
	protected ParametrosBalanca parametros;

	protected ComunicacaoBalanca(ParametrosBalanca parametros) {
		this.parametros = parametros;
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
				System.out.println("Enviou: " + cmd);

				int tentativas = 0;
				do {
					if (tentativas >= 1) {
						try { Thread.sleep(100); } catch (Exception ignored) {}
					}
					byte[] bytes = new byte[BYTES_BUFFER_SIZE];
					int readBytes = inputStream.read(bytes);
					if (readBytes >= 0) {
						bytes = Arrays.copyOf(bytes, readBytes);
					} else {
						bytes = EMPTY_ARRAY;
					}
					retorno = rightTrim(new String(bytes));
					System.out.println("Recebeu: [" + retorno.replace("\r", "\\r").replace("\n", "\\n") + "] bytes: " + Arrays.toString(bytes));
					tentativas++;
				} while (retorno.length() < minChars && tentativas < 10);

				String[] vals = retorno.trim().replace("\r", "").split("\n");
				if (vals.length > 1) {
					int maxLength = 0;
					for (String val : vals) {
						if (maxLength == 0 || val.length() >= maxLength) {
							maxLength = val.length();
							retorno = val;
						}
					}
					System.out.println("Considerando: " + retorno);
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
}
