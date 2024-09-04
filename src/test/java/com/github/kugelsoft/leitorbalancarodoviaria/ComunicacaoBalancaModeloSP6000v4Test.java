package com.github.kugelsoft.leitorbalancarodoviaria;

import com.github.kugelsoft.leitorbalancarodoviaria.modelos.ModeloBalanca;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ComunicacaoBalancaModeloSP6000v4Test extends TesteBalancaSocket {

    private ComunicacaoBalanca comunicacaoBalanca;

    @Before
    public void before() throws IOException {
        ParametrosBalanca parametros = new ParametrosBalanca("127.0.0.1", createSocket());
        parametros.setQuantidadeLeiturasConsiderarPesoEstavel(2);
        parametros.setMilissegundosEntreLeiturasConsiderarPesoEstavel(100);
        parametros.setPesoToleranciaConsiderarPesoEstavel(50);
        comunicacaoBalanca = ModeloBalanca.SP6000v4.getComunicacaoBalanca(parametros);
    }

    @Test
    public void lerPesoInvalido() throws Exception {
        enviar("xxxxxxxxx");

        PesoInvalidoException ex = null;
        try {
            comunicacaoBalanca.lerPeso();
        } catch (PesoInvalidoException e) {
            ex = e;
        }
        assertNotNull("Deveria ter gerado PesoInvalidoException", ex);
    }

    @Test
    public void testarConexao() throws Exception {
        enviar("");
        comunicacaoBalanca.testarConexao();
    }

    @Test
    public void lerPesoInstavel() throws Exception {
        enviar("€ 13680 \"?", "13680   ?");

        PesoInstavelException ex = null;
        try {
            comunicacaoBalanca.lerPeso();
        } catch (PesoInstavelException e) {
            ex = e;
        }
        assertNotNull("Deveria ter gerado PesoInstavelException", ex);
    }


    @Test
    public void lerPesoEstavel1() throws Exception {
        enviar("€ 13680 \"?", "22222  ?\n€ 13680 \"?");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(13680, peso.doubleValue(), 0);
    }

    @Test
    public void lerPesoEstavel2() throws Exception {
        enviar("€ 13680 \"?", " 13680  ?");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(13680, peso.doubleValue(), 0);
    }

}