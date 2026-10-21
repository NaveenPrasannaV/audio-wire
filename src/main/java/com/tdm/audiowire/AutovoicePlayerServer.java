package com.tdm.audiowire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class AutovoicePlayerServer {

    public static final Logger logger = LoggerFactory.getLogger(AutovoicePlayerServer.class);
    private static final int PORT = 5000;

    public static void main(String[] args) {
        logger.info("Starting Autovoice Player Server...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Listening on port {}", PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Client connected: {}", clientSocket.getInetAddress());
                AutoVoiceHandlerThread handler = new AutoVoiceHandlerThread(clientSocket);
                handler.start();
            }
        } catch (IOException e) {
            logger.error("Server error", e);
        }
    }
}
