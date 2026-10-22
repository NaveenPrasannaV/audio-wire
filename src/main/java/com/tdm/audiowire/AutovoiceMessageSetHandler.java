package com.tdm.audiowire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;

public class AutovoiceMessageSetHandler {
    private final Path xmlPath;
    private static final Logger logger = LoggerFactory.getLogger(AutovoiceMessageSetHandler.class);

    public AutovoiceMessageSetHandler(Path xmlPath) {
        this.xmlPath = xmlPath;
    }

    public AutovoiceMessageSet loadMessageSet(String group, String key) throws ParserConfigurationException, IOException, SAXException, IllegalAccessException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        Document document = factory.newDocumentBuilder().parse(xmlPath.toFile());
        Element root = document.getDocumentElement();
        NodeList messageSets = root.getElementsByTagName("MessageSet");

        for (int i = 0; i < messageSets.getLength(); i++) {
            Element element = (Element) messageSets.item(i);
            String configuredGroup = element.getAttribute("group");
            String configuredKey = element.getAttribute("key");
            if (group.equals(configuredGroup) && key.equals(configuredKey)) {
                String preWav = element.getElementsByTagName("preWav").item(0).getTextContent().trim();
                String postWav = element.getElementsByTagName("postWav").item(0).getTextContent().trim();
                double speed = Double.parseDouble(element.getElementsByTagName("speed").item(0).getTextContent().trim());
                logger.info("Loading message set: {}/{}", group, key);

                return new AutovoiceMessageSet(configuredGroup, configuredKey, preWav, postWav, speed);
            }
        }
        throw new IllegalAccessException("Message set not found: " + group + " / " + key);
    }
}
