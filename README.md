# k-leitor-balanca-rodoviaria

Biblioteca para comunicação com balanças rodoviárias via TCP/IP.

## Exemplo
```java
ParametrosBalanca parametros = new ParametrosBalanca("192.168.1.18", 4558);
parametros.setQuantidadeLeiturasConsiderarPesoEstavel(3);
parametros.setMilissegundosEntreLeiturasConsiderarPesoEstavel(1000);
parametros.setPesoToleranciaConsiderarPesoEstavel(50);

ComunicacaoBalanca comunicacaoBalanca = ModeloBalanca.SBR140.getComunicacaoBalanca(parametros);

// Testar conexão com a balança
try {
    comunicacaoBalanca.testarConexao();
} catch (IOException ex) {
    System.out.println("Não foi possível se conectar a balança.");
}

// Fazer a leitura do peso
try {
    BigDecimal peso = comunicacaoBalanca.lerPeso();
} catch (PesoInvalidoException ex) {
    System.out.println("Peso inválido.");
} catch (PesoInstavelException ex) {
    System.out.println("Peso instável.");
} catch (IOException ex) {
    System.out.println("Erro de comunicação com a balança.");
}
```

Para testes o modelo ModeloBalanca.TESTE pode ser utilizado, a ComunicacaoBalanca irá gerar um peso aleatório a cada chamada ao lerPeso() 

#### Desenvolvido por [Kugel Soft Informática LTDA](https://www.kugel.com.br)
