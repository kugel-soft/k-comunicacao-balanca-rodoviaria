package com.github.kugelsoft.leitorbalancarodoviaria;

import com.github.kugelsoft.leitorbalancarodoviaria.modelos.ModeloBalanca;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ComunicacaoBalancaModeloIND560Test extends TesteBalancaSocket {

    private ComunicacaoBalanca comunicacaoBalanca;

    @Before
    public void before() throws IOException {
        ParametrosBalanca parametros = new ParametrosBalanca("127.0.0.1", createSocket());
        parametros.setQuantidadeLeiturasConsiderarPesoEstavel(4);
        parametros.setMilissegundosEntreLeiturasConsiderarPesoEstavel(100);
        parametros.setPesoToleranciaConsiderarPesoEstavel(50);
        comunicacaoBalanca = ModeloBalanca.IND560.getComunicacaoBalanca(parametros);
    }

    @Test
    public void lerPesoInvalido() throws Exception {
        enviar("xxxxxxxxxxxxxxxxxxxxxxx");

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
        enviar(" ");
        comunicacaoBalanca.testarConexao();
    }

    @Test
    public void lerPesoInstavel() throws Exception {
        enviar("xxxx01486000015687aaa", "xxxx015860000123");

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
        enviar("xxxx01486000015687aaa", "xxxx01487000000000aaa", "xxxx01487000000000aaa", "xxxx01489000000000aaa");

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(14860, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso2() throws Exception {
        String strPeso = new String(new byte[] { 2, 41, 48, 32, 32, 49, 57, 49, 53, 48, 32, 32, 32, 32, 48, 48, 13 });

        enviar(strPeso, strPeso, strPeso, strPeso);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(19150, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso3() throws Exception {
        String strPeso = new String(new byte[] { 2, 41, 48, 32, 32, 32, 57, 57, 57, 48, 32, 32, 32, 32, 48, 48, 13 });

        enviar(strPeso, strPeso, strPeso, strPeso);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(9990, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso4() throws Exception {
        String strPeso = new String(new byte[] { 2, 41, 48, 32, 49, 48, 48, 48, 48, 48, 48, 48, 32, 32, 48, 48, 13 });

        enviar(strPeso, strPeso, strPeso, strPeso);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(100000, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso5() throws Exception {
        String strPeso = new String(new byte[] { 2, 105, 32, 32, 32, 52, 55, 49, 57, 48, 48, 48, 48, 48, 48, 48, 13, 3 });

        enviar(strPeso, strPeso, strPeso, strPeso);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(47190, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso6() throws Exception {
        String strPeso = new String(new byte[] { 2, 105, 32, 32, 32, 49, 54, 57, 54, 48, 48, 48, 48, 48, 48, 48, 13, 2 });

        enviar(strPeso, strPeso, strPeso, strPeso);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(16960, peso.doubleValue(), 0);
    }

    @Test
    public void lerPeso7() throws Exception {
        String strPeso = new String(new byte[] { 2, 105, 32, 32, 32, 52, 57, 56, 56, 48, 48, 48, 48, 48, 48, 48, 13, 123 });

        enviar(strPeso, strPeso, strPeso, strPeso);

        BigDecimal peso = comunicacaoBalanca.lerPeso();
        assertEquals(49880, peso.doubleValue(), 0);
    }
}