package com.github.kugelsoft.leitorbalancarodoviaria;

import org.junit.After;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TesteBalancaSocket {
    private ServerSocket server;

    public int createSocket() throws IOException {
        server = new ServerSocket(0);
        System.out.println("Abriu porta: " + server.getLocalPort());
        return server.getLocalPort();
    }

    public void enviar(final String... strs) throws IOException {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    for (String str : strs) {
                        Socket socket = server.accept();
                        socket.getInputStream().read(new byte[1]);
                        socket.getOutputStream().write(str.getBytes("UTF-8"));
                        socket.getOutputStream().flush();
                        socket.close();
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        };
        t.start();
    }

    @After
    public void after() throws IOException {
        System.out.println("Fechando porta: " + server.getLocalPort());
        server.close();
    }
}
