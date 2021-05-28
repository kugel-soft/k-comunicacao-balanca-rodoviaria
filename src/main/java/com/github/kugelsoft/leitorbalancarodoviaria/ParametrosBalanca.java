package com.github.kugelsoft.leitorbalancarodoviaria;

public class ParametrosBalanca {

    private final String ip;
    private final int porta;
    private int quantidadeLeiturasConsiderarPesoEstavel;
    private int milissegundosEntreLeiturasConsiderarPesoEstavel;
    private int pesoToleranciaConsiderarPesoEstavel;

    /**
     * Cria um novo parâmetro definindo o modelo, o ip e a porta para fazer a leitura da balança
     * @param ip Ip da balança
     * @param porta Porta da balança
     */
    public ParametrosBalanca(String ip, int porta) {
        this.ip = ip;
        this.porta = porta;
        this.quantidadeLeiturasConsiderarPesoEstavel = 1;
        this.milissegundosEntreLeiturasConsiderarPesoEstavel = 500;
        this.pesoToleranciaConsiderarPesoEstavel = 20;
    }

    public String getIp() {
        return ip;
    }

    public int getPorta() {
        return porta;
    }

    public int getQuantidadeLeiturasConsiderarPesoEstavel() {
        return quantidadeLeiturasConsiderarPesoEstavel;
    }

    /**
     * Define a quantidade de leituras que deve fazer para considerar o peso como estável <br>
     * Valor padrão: 1
     * @param quantidadeLeiturasConsiderarPesoEstavel quantidade de leituras
     */
    public void setQuantidadeLeiturasConsiderarPesoEstavel(int quantidadeLeiturasConsiderarPesoEstavel) {
        this.quantidadeLeiturasConsiderarPesoEstavel = quantidadeLeiturasConsiderarPesoEstavel;
    }

    public int getMilissegundosEntreLeiturasConsiderarPesoEstavel() {
        return milissegundosEntreLeiturasConsiderarPesoEstavel;
    }

    /**
     * Define quantos milissegundos deve esperar entre as leituras para considerar o peso como estável <br>
     * Utilizado apenas quando quantidadeLeiturasConsiderarPesoEstavel &gt; 1
     * Valor padrão: 500
     * @param milissegundosEntreLeiturasConsiderarPesoEstavel milissegundos entre leituras
     */
    public void setMilissegundosEntreLeiturasConsiderarPesoEstavel(int milissegundosEntreLeiturasConsiderarPesoEstavel) {
        this.milissegundosEntreLeiturasConsiderarPesoEstavel = milissegundosEntreLeiturasConsiderarPesoEstavel;
    }

    public int getPesoToleranciaConsiderarPesoEstavel() {
        return pesoToleranciaConsiderarPesoEstavel;
    }

    /**
     * Define a tolerância de peso que pode ter para mais ou para menos para considerar o peso como estável <br>
     * Utilizado apenas quando quantidadeLeiturasConsiderarPesoEstavel &gt; 1
     * Valor padrão: 20
     * @param pesoToleranciaConsiderarPesoEstavel peso de tolerância
     */
    public void setPesoToleranciaConsiderarPesoEstavel(int pesoToleranciaConsiderarPesoEstavel) {
        this.pesoToleranciaConsiderarPesoEstavel = pesoToleranciaConsiderarPesoEstavel;
    }
}
