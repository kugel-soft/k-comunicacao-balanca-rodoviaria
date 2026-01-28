package com.github.kugelsoft.leitorbalancarodoviaria;

import com.github.kugelsoft.leitorbalancarodoviaria.modelos.ModeloBalanca;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ComunicacaoBalancaModeloBC30Test extends TesteBalancaSocket {

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

    @Test
    public void lerPesoEstavel2() throws Exception {
        enviar(" 029460OL_", " 029460OL_", " 029460OL_");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(29460, peso.doubleValue(), 0);
    }

    @Test
    public void lerPesoEstavel3() throws Exception {
        enviar("\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r001234\n\r022020\n\r022020\n\r022020\n", "\r022020\n\r022020\n\r022020\n", "022020\r022020");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(22020, peso.doubleValue(), 0);
    }

    @Test
    public void lerPesoEstavel4() throws Exception {
        String str = new String(new byte[] {13, 49, 48, 51, 50, 50, 48, 69, 76, 95, 32, 10});
        enviar(str, str, str);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(103220, peso.doubleValue(), 0);
    }


    @Test
    public void lerPesoEstavel5() throws Exception {
        String str = new String(new byte[] {13, 48, 48, 48, 48, 48, 48, 69, 76, 95, 32, 10});
        enviar(str, str, str);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(0, peso.doubleValue(), 0);
    }

    @Test
    public void lerPesoEstavel6() throws Exception {
        String str = new String(new byte[] {13, 48, 48, 48, 48, 48, 48, 10, 13, 48, 48, 48, 48, 48, 48, 10, 13, 48, 48, 48, 48, 48, 48, 10, 13, 48, 48, 48, 48, 48, 48, 10});
        enviar(str, str, str);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(0, peso.doubleValue(), 0);
    }

    @Test
    public void lerPesoEstavel7() throws Exception {
        String str = new String(new byte[] {13, 45, 48, 48, 48, 56, 48, 69, 76, 95, 32, 10});
        enviar(str, str, str);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(80, peso.doubleValue(), 0);
    }

    @Test
    public void lerPesoEstavel8() throws Exception {
        String str = new String(new byte[] {13, 45, 48, 48, 48, 56, 48, 69, 76, 95, 32, 10});
        enviar(str, str, str);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(80, peso.doubleValue(), 0);
    }

}