package com.github.kugelsoft.leitorbalancarodoviaria;

import com.github.kugelsoft.leitorbalancarodoviaria.modelos.ModeloBalanca;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ComunicacaoBalancaModeloWT3000ITest extends TesteBalancaSocket {

    private ComunicacaoBalanca comunicacaoBalanca;

    @Before
    public void before() throws IOException {
        ParametrosBalanca parametros = new ParametrosBalanca("127.0.0.1", createSocket());
        parametros.setQuantidadeLeiturasConsiderarPesoEstavel(2);
        parametros.setMilissegundosEntreLeiturasConsiderarPesoEstavel(100);
        parametros.setPesoToleranciaConsiderarPesoEstavel(50);
        comunicacaoBalanca = ModeloBalanca.WT3000I.getComunicacaoBalanca(parametros);
    }

    @Test
    public void lerPesoInvalido() throws Exception {
        enviar("xxxxxxxxxxxxxxxxxxxxxxxxxx");

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
        enviar("US,GS,+ 133960  kg", "US,GS,+ 134960  kg");

        PesoInstavelException ex = null;
        try {
            comunicacaoBalanca.lerPeso();
        } catch (PesoInstavelException e) {
            ex = e;
        }
        assertNotNull("Deveria ter gerado PesoInstavelException", ex);
    }


    @Test
    public void lerPesoEstavel() throws Exception {
        enviar("US,GS,+ 133960  kg", "US,GS,+ 133960  kg");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(133960, peso.doubleValue(), 0);
    }

    @Test
    public void lerPesoEstavelZero() throws Exception {
        enviar("US,GS,+      0  kg", "kg\r\nST,GS,+      0  kg\n");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(0, peso.doubleValue(), 0);
    }
}
