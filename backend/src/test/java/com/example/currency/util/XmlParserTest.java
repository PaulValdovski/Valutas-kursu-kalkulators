package com.example.currency.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class XmlParserTest {

    private XmlParser xmlParser;

    @BeforeEach
    void setUp() {
        xmlParser = new XmlParser();
    }

    @Test
    void testParse_NormalXml() {
        String xml = """
                <Rates>
                    <Currency>
                        <ID>USD</ID>
                        <Rate>1.1</Rate>
                    </Currency>
                    <Currency>
                        <ID>GBP</ID>
                        <Rate>0.9</Rate>
                    </Currency>
                </Rates>
                """;

        Map<String, Double> result = xmlParser.parse(xml);

        assertEquals(3, result.size());
        assertEquals(1.1, result.get("USD"));
        assertEquals(0.9, result.get("GBP"));
        assertEquals(1.0, result.get("EUR"));
    }

    @Test
    void testParse_WithErrorNode() {
        String xml = "<Rates><Error>Some error occurred</Error></Rates>";

        RuntimeException ex = assertThrows(RuntimeException.class, () -> xmlParser.parse(xml));
        assertTrue(ex.getMessage().contains("Bank.lv returned an error"));
    }

    @Test
    void testParse_MalformedXml() {
        String xml = "<Rates><Currency><ID>USD</ID><Rate>1.1</Rate></Currency"; // missing closing >

        RuntimeException ex = assertThrows(RuntimeException.class, () -> xmlParser.parse(xml));
        assertTrue(ex.getMessage().contains("Failed to parse"));
    }

    @Test
    void testParse_EmptyXml() {
        String xml = "";

        RuntimeException ex = assertThrows(RuntimeException.class, () -> xmlParser.parse(xml));
        assertTrue(ex.getMessage().contains("Failed to parse"));
    }

    @Test
    void testParse_MissingRateOrId() {
        String xml = """
                <Rates>
                    <Currency>
                        <ID>JPY</ID>
                    </Currency>
                    <Currency>
                        <Rate>0.8</Rate>
                    </Currency>
                </Rates>
                """;

        RuntimeException ex = assertThrows(RuntimeException.class, () -> xmlParser.parse(xml));
        assertTrue(ex.getMessage().contains("Failed to parse"));
    }

}
