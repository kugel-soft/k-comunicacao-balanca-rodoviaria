package com.github.kugelsoft.leitorbalancarodoviaria.modelos;

import com.github.kugelsoft.leitorbalancarodoviaria.ComunicacaoBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.ParametrosBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.PesoInstavelException;
import com.github.kugelsoft.leitorbalancarodoviaria.PesoInvalidoException;

import java.io.IOException;
import java.math.BigDecimal;

class ComunicacaoBalancaModeloSP6000v1 extends ComunicacaoBalanca {

	protected ComunicacaoBalancaModeloSP6000v1(ParametrosBalanca parametros) {
		super(parametros);
	}

	protected BigDecimal lerPesoModelo() throws IOException, PesoInvalidoException, PesoInstavelException {
		String retorno = enviarComando("G");

		String tipo = substring(retorno, 0, 1);
		String peso = substring(retorno, 1, 8);
		String estabilidade = substring(retorno, 8, 9);

		if (tipo.equals("g")) {
			if (estabilidade.equals("E")) {
				try {
					return new BigDecimal(peso.trim());
				} catch (NumberFormatException ex) {
					throw new PesoInvalidoException(ex);
				}
			}
			if (estabilidade.equals("I") || estabilidade.equals("O")) {
				throw new PesoInstavelException();
			}
		}
		throw new PesoInvalidoException();
	}

	public void testarConexao() throws IOException {
		enviarComando("K");
	}
}
