package com.github.kugelsoft.leitorbalancarodoviaria;

import com.github.kugelsoft.leitorbalancarodoviaria.modelos.ModeloBalanca;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ComunicacaoBalancaModeloTesteTest {

    private static ComunicacaoBalanca comunicacaoBalanca;

    @BeforeClass
    public static void criarBalanca() {
        ParametrosBalanca parametros = new ParametrosBalanca("", 0);
        comunicacaoBalanca = ModeloBalanca.TESTE.getComunicacaoBalanca(parametros);
    }

    @Test
    public void lerPeso() throws Exception {
        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertTrue(peso.doubleValue() > 0);
    }

    @Test
    public void testarConexao() throws Exception {
        comunicacaoBalanca.testarConexao();
    }
}