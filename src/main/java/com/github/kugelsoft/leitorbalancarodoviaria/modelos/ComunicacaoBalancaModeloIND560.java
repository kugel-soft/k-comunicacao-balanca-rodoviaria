package com.github.kugelsoft.leitorbalancarodoviaria.modelos;

import com.github.kugelsoft.leitorbalancarodoviaria.ComunicacaoBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.ParametrosBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.PesoInvalidoException;

import java.io.IOException;
import java.math.BigDecimal;

class ComunicacaoBalancaModeloIND560 extends ComunicacaoBalanca {

	protected ComunicacaoBalancaModeloIND560(ParametrosBalanca parametros) {
		super(parametros);
	}

	protected BigDecimal lerPesoModelo() throws IOException, PesoInvalidoException {
		String retorno = enviarComando(" ", 15);

		String peso = substring(retorno, 4, 10);

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
