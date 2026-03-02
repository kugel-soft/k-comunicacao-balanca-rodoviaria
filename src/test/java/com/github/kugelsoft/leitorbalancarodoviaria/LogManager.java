package com.github.kugelsoft.leitorbalancarodoviaria;

import java.io.IOException;
import java.io.InputStream;

public class LogManager extends java.util.logging.LogManager {

    private boolean carregou = false;

    public LogManager() {
        try {
            readConfiguration(getClass().getResourceAsStream("/logging.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void readConfiguration(InputStream ins) throws IOException, SecurityException {
        if (!carregou) {
            super.readConfiguration(ins);
            carregou = true;
        }
    }
}
