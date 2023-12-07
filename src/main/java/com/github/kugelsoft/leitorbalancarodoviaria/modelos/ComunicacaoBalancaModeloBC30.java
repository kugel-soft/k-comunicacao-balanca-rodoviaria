package com.github.kugelsoft.leitorbalancarodoviaria.modelos;

import com.github.kugelsoft.leitorbalancarodoviaria.ComunicacaoBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.ParametrosBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.PesoInvalidoException;

import java.io.IOException;
import java.math.BigDecimal;

class ComunicacaoBalancaModeloBC30 extends ComunicacaoBalanca {

	protected ComunicacaoBalancaModeloBC30(ParametrosBalanca parametros) {
		super(parametros);
	}

	protected BigDecimal lerPesoModelo() throws IOException, PesoInvalidoException {
		String retorno = enviarComando(" ", 10);

		String[] vals = retorno.trim().replace("\r", "").split("\n");
		if (vals.length > 1) {
			retorno = vals[vals.length - 2];
		}

		String peso = substring(retorno.trim(), 1, 7);
		if (!peso.matches("[0-9]+")) {
			peso = substring(retorno.trim(), 1, 6);
		}

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
