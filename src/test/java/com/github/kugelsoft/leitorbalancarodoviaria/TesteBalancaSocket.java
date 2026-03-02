package com.github.kugelsoft.leitorbalancarodoviaria;

import org.junit.After;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TesteBalancaSocket {
    private ServerSocket server;
    private boolean modoContinuo;

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
                    if (modoContinuo) {
                        Socket socket = server.accept();
                        while (modoContinuo) {
                            for (String str : strs) {
                                try {
                                    socket.getOutputStream().write(str.getBytes("UTF-8"));
                                    socket.getOutputStream().flush();
                                    Thread.sleep(10);
                                } catch (SocketException ex) {
                                    if (modoContinuo) {
                                        socket = server.accept();
                                    }
                                }
                            }
                        }
                        socket.close();
                    } else {
                        for (String str : strs) {
                            Socket socket = server.accept();
                            for (int i = 0; i < 2; i++) {
                                try {
                                    socket.getInputStream().read(new byte[1]);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                socket.getOutputStream().write(str.getBytes("UTF-8"));
                                socket.getOutputStream().flush();
                            }
                            socket.close();
                        }
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void setModoContinuo(boolean modoContinuo) {
        this.modoContinuo = modoContinuo;
    }

    @After
    public void after() throws IOException {
        System.out.println("Fechando porta: " + server.getLocalPort());
        server.close();
    }
}
