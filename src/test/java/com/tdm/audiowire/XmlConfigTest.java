package com.tdm.audiowire;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;

public class XmlConfigTest {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, IllegalAccessException {
        AutovoiceMessageSetHandler handler = new AutovoiceMessageSetHandler(Path.of("config", "AutovoiceMessageSets.xml"));
        AutovoiceMessageSet messageSet = handler.loadMessageSet("MRI", "START_SCAN");

        System.out.println("Group: " + messageSet.getGroup());
        System.out.println("Key: " + messageSet.getKey());
        System.out.println("Pre WAV: " + messageSet.getPreWav());
        System.out.println("Post WAV: " + messageSet.getPostWav());
        System.out.println("Speed: " + messageSet.getSpeed());

    }
}
