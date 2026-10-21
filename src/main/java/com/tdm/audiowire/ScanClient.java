package com.tdm.audiowire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ScanClient {
    private static final Logger logger = LoggerFactory.getLogger(ScanClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) throws IOException {
        logger.info("Scan Client starting...");
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            logger.info("Connected to Autovoice Player Server");
            writer.println("PING");
            logger.info("Sent command: PING");
            String response = reader.readLine();
            logger.info("Received response: {}", response);
        } catch (IOException e) {
            logger.error("Could not connect to Autovoice Player Server", e);
        }
    }

}
