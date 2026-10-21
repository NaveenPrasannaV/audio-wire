package com.tdm.audiowire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AutoVoiceHandlerThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(AutoVoiceHandlerThread.class);
    private final Socket clientSocket;

    public AutoVoiceHandlerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (Socket socket = clientSocket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        ) {
            logger.info("Handling client: {}", socket.getRemoteSocketAddress());
            String command = reader.readLine();
            logger.info("Received command: {}", command);
            if ("PING".equals(command)) {
                writer.println("PONG");
            } else {
                writer.println("UNKNOWN_COMMAND");
            }
        } catch (IOException e) {
            logger.error("Error while handling client connection", e);
        }
    }
}
