package com.example.currency.util;

import org.springframework.stereotype.Component;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class XmlParser {

    public Map<String, Double> parse(String xmlData) {
        try {
            Map<String, Double> rates = new HashMap<>();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            NodeList errorNodes = doc.getElementsByTagNameNS("*", "Error");
            if (errorNodes.getLength() > 0) {
                throw new RuntimeException("Bank.lv returned an error: " + errorNodes.item(0).getTextContent());
            }

            NodeList currencyNodes = doc.getElementsByTagNameNS("*", "Currency");
            for (int i = 0; i < currencyNodes.getLength(); i++) {
                Node currencyNode = currencyNodes.item(i);
                if (currencyNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element currencyElement = (Element) currencyNode;
                    String id = currencyElement.getElementsByTagNameNS("*", "ID").item(0).getTextContent();
                    String rateText = currencyElement.getElementsByTagNameNS("*", "Rate").item(0).getTextContent();
                    double rate = Double.parseDouble(rateText);
                    rates.put(id, rate);
                }
            }

            rates.put("EUR", 1.0);

            return rates;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Bank.lv XML: " + e.getMessage(), e);
        }
    }
}
