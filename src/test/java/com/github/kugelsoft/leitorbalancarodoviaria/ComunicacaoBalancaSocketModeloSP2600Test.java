package com.github.kugelsoft.leitorbalancarodoviaria;

import com.github.kugelsoft.leitorbalancarodoviaria.modelos.ModeloBalanca;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ComunicacaoBalancaSocketModeloSP2600Test extends TesteBalancaSocket {

    private ComunicacaoBalanca comunicacaoBalanca;

    @Before
    public void before() throws IOException {
        ParametrosBalanca parametros = new ParametrosBalanca("127.0.0.1", createSocket());
        parametros.setQuantidadeLeiturasConsiderarPesoEstavel(2);
        parametros.setMilissegundosEntreLeiturasConsiderarPesoEstavel(100);
        parametros.setPesoToleranciaConsiderarPesoEstavel(200);
        comunicacaoBalanca = ModeloBalanca.SP2600.getComunicacaoBalanca(parametros);
    }

    @Test
    public void lerPesoInvalido() throws Exception {
        enviar("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

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
        enviar("xxxx001486000015687aaaooooooooooooo", "xxxx0015860000123ooooooooooooo");

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
        enviar("xxxx001486000015687aaaoooooooo", "xxxx001506000000000aaaoooooooo");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(14860, peso.doubleValue(), 0);
    }
}