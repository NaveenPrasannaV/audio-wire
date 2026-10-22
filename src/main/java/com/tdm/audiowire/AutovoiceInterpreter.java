package com.tdm.audiowire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class AutovoiceInterpreter {
    private static final Logger logger = LoggerFactory.getLogger(AutovoiceInterpreter.class);

    private final AutovoiceMessageSetHandler messageSetHandler;
    private final AutovoicePlayer player;
    private AutovoiceMessageSet loadedMessageSet;

    public AutovoiceInterpreter() {
        this.player = new AutovoicePlayer();
        this.messageSetHandler = new AutovoiceMessageSetHandler(Path.of("config", "AutovoiceMessageSets.xml"));
    }

    public String evaluateCommand(String command) {
        if (AutovoiceCommand.PING.equals(command)) {
            return "PONG";
        }
        if (AutovoiceCommand.LOAD.equals(command)) {
            try {
                loadedMessageSet = messageSetHandler.loadMessageSet("MRI", "START_SCAN");
                logger.info("Loaded message set: {}/{}",
                        loadedMessageSet.getGroup(),
                        loadedMessageSet.getKey());

                logger.info("Pre audio: {}", loadedMessageSet.getPreWav());
                logger.info("Post audio: {}", loadedMessageSet.getPostWav());
                logger.info("Playback speed: {}", loadedMessageSet.getSpeed());

                return "OP_AUTOVOICE_LOAD_RESP:SUCCESS";
            } catch (Exception e) {
                logger.error("Failed to load Autovoice message set", e);
                return "OP_AUTOVOICE_LOAD_RESP:FAILED";
            }
        }

        if (AutovoiceCommand.PLAY_PRE.equals(command)) {
            if (loadedMessageSet == null) {
                logger.warn("Cannot play pre audio: message set not loaded");
                return "OP_AUTOVOICE_PLAY_PRE_RESP:FAILED";
            }
            try {
                player.play(loadedMessageSet.getPreWav());
                return "OP_AUTOVOICE_PLAY_PRE_RESP:SUCCESS";
            } catch (Exception e) {
                logger.error("Failed to play pre audio", e);
                return "OP_AUTOVOICE_PLAY_PRE_RESP:FAILED";
            }
        }
        logger.warn("Unknown command: {}", command);
        return "UNKNOWN_COMMAND";
    }
}
