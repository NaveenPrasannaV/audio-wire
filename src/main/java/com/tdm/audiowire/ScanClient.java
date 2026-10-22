
package com.tdm.audiowire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ScanClient {

    private static final Logger logger =
            LoggerFactory.getLogger(ScanClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {

        logger.info("Scan Client starting...");

        try (
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            logger.info("Connected to Autovoice Player Server");

            // Step 1: Load the message set.
            sendCommand(writer, reader, AutovoiceCommand.LOAD);

            // Step 2: Play pre-scan audio.
            sendCommand(writer, reader, AutovoiceCommand.PLAY_PRE);

            // Step 3: Play post-scan audio.
            sendCommand(writer, reader, AutovoiceCommand.PLAY_POST);

            logger.info("All commands processed.");

        } catch (IOException e) {
            logger.error(
                    "Could not connect to Autovoice Player Server", e);
        }
    }

    private static void sendCommand(
            PrintWriter writer,
            BufferedReader reader,
            String command) throws IOException {

        logger.info("Sending command: {}", command);
        writer.println(command);

        String response = reader.readLine();

        if (response == null) {
            throw new IOException(
                    "Server closed the connection before responding to: "
                            + command);
        }

        logger.info("Received response: {}", response);
    }
}
