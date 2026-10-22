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
            writer.println(AutovoiceCommand.LOAD);
            logger.info("Sent command: {}", AutovoiceCommand.LOAD);
            String response = reader.readLine();
            logger.info("Received response: {}", response);

            writer.println(AutovoiceCommand.PLAY_PRE);
            logger.info("Sent command: {}", AutovoiceCommand.PLAY_PRE);

            response = reader.readLine();
            logger.info("Received response: {}", response);

        } catch (IOException e) {
            logger.error("Could not connect to Autovoice Player Server", e);
        }
    }

}
