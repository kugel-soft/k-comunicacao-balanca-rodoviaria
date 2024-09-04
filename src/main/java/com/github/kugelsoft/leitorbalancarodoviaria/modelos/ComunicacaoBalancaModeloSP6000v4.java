package com.github.kugelsoft.leitorbalancarodoviaria.modelos;

import com.github.kugelsoft.leitorbalancarodoviaria.ComunicacaoBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.ParametrosBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.PesoInvalidoException;

import java.io.IOException;
import java.math.BigDecimal;

class ComunicacaoBalancaModeloSP6000v4 extends ComunicacaoBalanca {

	protected ComunicacaoBalancaModeloSP6000v4(ParametrosBalanca parametros) {
		super(parametros);
	}

	protected BigDecimal lerPesoModelo() throws IOException, PesoInvalidoException {
		String retorno = enviarComando(" ", 9);

		String peso = substring(retorno.replace("o", " "), 1, 7);

		try {
			return new BigDecimal(peso.trim());
		} catch (NumberFormatException ex) {
			throw new PesoInvalidoException(ex);
		}
	}

	public void testarConexao() throws IOException {
		enviarComando(" ");
	}

}
