package com.github.kugelsoft.leitorbalancarodoviaria.modelos;

import com.github.kugelsoft.leitorbalancarodoviaria.ComunicacaoBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.ParametrosBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.PesoInvalidoException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

class ComunicacaoBalancaModeloTeste extends ComunicacaoBalanca {

	protected ComunicacaoBalancaModeloTeste(ParametrosBalanca parametros) {
		super(parametros);
	}

	protected BigDecimal lerPesoModelo() throws IOException, PesoInvalidoException {
		return new BigDecimal(new Random().nextInt(56) * 1000 + 100);
	}

	public void testarConexao() {
	}
}
