package com.github.kugelsoft.leitorbalancarodoviaria;

import com.github.kugelsoft.leitorbalancarodoviaria.modelos.ModeloBalanca;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ComunicacaoBalancaModeloSP6000v3Test extends TesteBalancaSocket {

    private ComunicacaoBalanca comunicacaoBalanca;

    @Before
    public void before() throws IOException {
        ParametrosBalanca parametros = new ParametrosBalanca("127.0.0.1", createSocket());
        parametros.setQuantidadeLeiturasConsiderarPesoEstavel(2);
        parametros.setMilissegundosEntreLeiturasConsiderarPesoEstavel(100);
        parametros.setPesoToleranciaConsiderarPesoEstavel(50);
        comunicacaoBalanca = ModeloBalanca.SP6000v3.getComunicacaoBalanca(parametros);
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
        enviar("xxxxx010000xxxxxxxxxxxxxxx", "xxxxx015000xxxxxxxxxxxxxxx");

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
        enviar("xxxx0100050xxxxxxxxxxxxxxx", "xxxx0100020xxxxxxxxxxxxxxx");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(100050, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso1() throws Exception {
        enviar("    00334000110033400000000000", "    00334000110033400000000000");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(33400, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso2() throws Exception {
        enviar(" 00299000110029900000000000", " 00299000110029900000000000");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(29900, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso3() throws Exception {
        enviar("     00174700110017470000000000", "     00174700110017470000000000");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(17470, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso4() throws Exception {
        enviar("00035600110003560000000000", "00035600110003560000000000");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(3560, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso5() throws Exception {
        enviar("abc01035600110003560000000000", "abc01035600110003560000000000");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(103560, peso.doubleValue(), 0);
    }
}