package com.tdm.audiowire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class AutovoiceInterpreter {
    private static final Logger logger = LoggerFactory.getLogger(AutovoiceInterpreter.class);

    private final AutovoiceMessageSetHandler messageSetHandler;

    public AutovoiceInterpreter() {
        this.messageSetHandler = new AutovoiceMessageSetHandler(Path.of("config", "AutovoiceMessageSets.xml"));
    }

    public String evaluateCommand(String command) {
        if (AutovoiceCommand.PING.equals(command)) {
            return "PONG";
        }
        if (AutovoiceCommand.LOAD.equals(command)) {
            try {
                AutovoiceMessageSet messageSet = messageSetHandler.loadMessageSet("MRI", "START_SCAN");
                logger.info("Loaded message set: {}/{}",
                        messageSet.getGroup(),
                        messageSet.getKey());

                logger.info("Pre audio: {}", messageSet.getPreWav());
                logger.info("Post audio: {}", messageSet.getPostWav());
                logger.info("Playback speed: {}", messageSet.getSpeed());

                return "OP_AUTOVOICE_LOAD_RESP:SUCCESS";
            } catch (Exception e) {
                logger.error("Failed to load Autovoice message set", e);
                return "OP_AUTOVOICE_LOAD_RESP:FAILED";
            }
        }
        logger.warn("Unknown command: {}", command);
        return "UNKNOWN_COMMAND";
    }
}
