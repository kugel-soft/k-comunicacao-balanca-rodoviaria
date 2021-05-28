package com.github.kugelsoft.leitorbalancarodoviaria;

public class PesoInvalidoException extends Exception {

    public PesoInvalidoException() {
    }

    public PesoInvalidoException(Exception cause) {
        super("Peso inválido.", cause);
    }

    public PesoInvalidoException(String message) {
        super(message);
    }
}
