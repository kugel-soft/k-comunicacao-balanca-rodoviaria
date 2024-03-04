package com.github.kugelsoft.leitorbalancarodoviaria.modelos;

import com.github.kugelsoft.leitorbalancarodoviaria.ComunicacaoBalanca;
import com.github.kugelsoft.leitorbalancarodoviaria.ParametrosBalanca;

public enum ModeloBalanca {
    TESTE("TESTE", ComunicacaoBalancaModeloTeste.class),
    SP2600("SP-2600", ComunicacaoBalancaModeloSP2600.class),
    SP6000v1("SP-6000 v1", ComunicacaoBalancaModeloSP6000v1.class),
    SP6000v2("SP-6000 v2", ComunicacaoBalancaModeloSP6000v2.class),
    SP6000v3("SP-6000 v3", ComunicacaoBalancaModeloSP6000v3.class),
    SBR140("SBR-140", ComunicacaoBalancaModeloSBR140.class),
    IND560("IND-560", ComunicacaoBalancaModeloIND560.class),
    BC30("BC-30", ComunicacaoBalancaModeloBC30.class),
    WT3000I("WT3000-I", ComunicacaoBalancaModeloWT3000I.class);

    private final String descricao;
    private final Class<? extends ComunicacaoBalanca> classeComunicao;

    ModeloBalanca(String descricao, Class<? extends ComunicacaoBalanca> classe) {
        this.descricao = descricao;
        this.classeComunicao = classe;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Obtem uma instancia de ComunicacaoBalanca de acordo com o parametrosBalanca
     * @param parametrosBalanca Parâmetros com o modelo e as configurações de comunicação com a balança
     * @return uma nova instancia de ComunicacaoBalanca
     * @throws RuntimeException caso ocorra algum erro ao instanciar o ComunicacaoBalanca
     */
    public ComunicacaoBalanca getComunicacaoBalanca(ParametrosBalanca parametrosBalanca) {
        try {
            return classeComunicao
                    .getDeclaredConstructor(ParametrosBalanca.class)
                    .newInstance(parametrosBalanca);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao criar objeto da clase ComunicacaoBalanca para o modelo " + this, ex);
        }
    }

}
