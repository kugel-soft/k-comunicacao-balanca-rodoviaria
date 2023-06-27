package com.github.kugelsoft.leitorbalancarodoviaria;

import com.github.kugelsoft.leitorbalancarodoviaria.modelos.ModeloBalanca;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ComunicacaoBalancaSocketModeloBC30Test extends TesteBalancaSocket {

    private ComunicacaoBalanca comunicacaoBalanca;

    @Before
    public void before() throws IOException {
        ParametrosBalanca parametros = new ParametrosBalanca("127.0.0.1", createSocket());
        parametros.setQuantidadeLeiturasConsiderarPesoEstavel(3);
        parametros.setMilissegundosEntreLeiturasConsiderarPesoEstavel(100);
        parametros.setPesoToleranciaConsiderarPesoEstavel(50);
        comunicacaoBalanca = ModeloBalanca.BC30.getComunicacaoBalanca(parametros);
    }

    @Test
    public void testarConexao() throws Exception {
        enviar("");
        comunicacaoBalanca.testarConexao();
    }

    @Test
    public void lerPesoInvalido() throws Exception {
        enviar("xxxxxxxxxxxxxxxxx");

        PesoInvalidoException ex = null;
        try {
            comunicacaoBalanca.lerPeso();
        } catch (PesoInvalidoException e) {
            ex = e;
        }
        assertNotNull("Deveria ter gerado PesoInvalidoException", ex);
    }

    @Test
    public void lerPesoInstavel() throws Exception {
        enviar("x083221029", " 012345000");

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
        enviar("x083221029", "    -083271029", "a083191029");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(83221, peso.doubleValue(), 0);
    }

}