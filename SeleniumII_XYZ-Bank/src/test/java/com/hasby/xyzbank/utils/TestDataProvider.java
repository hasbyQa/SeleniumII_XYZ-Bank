package com.hasby.xyzbank.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.provider.Arguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// Reads JSON test data files and provides them to @ParameterizedTest methods
// JSON chosen over CSV because it supports complex/nested data structures
public class TestDataProvider {
    private static final Logger logger = LoggerFactory.getLogger(TestDataProvider.class);
    // Jackson ObjectMapper — converts JSON to Java objects
    private static final ObjectMapper mapper = new ObjectMapper();

    // Reads a JSON array file and returns each object's fields as test arguments
    private static List<JsonNode> readJsonArray(String resourcePath) {
        try {
            // Load file from src/test/resources/
            InputStream is = TestDataProvider.class.getResourceAsStream(resourcePath);
            // Parse JSON array into a list of nodes
            JsonNode root = mapper.readTree(is);
            List<JsonNode> nodes = new ArrayList<>();
            root.forEach(nodes::add);
            logger.info("Loaded {} records from {}", nodes.size(), resourcePath);
            return nodes;
        } catch (Exception e) {
            logger.error("Failed to read JSON: {}", resourcePath, e);
            return List.of();
        }
    }

    // Provides customer data for @ParameterizedTest — each JSON object becomes one test run
    public static Stream<Arguments> customerData() {
        return readJsonArray("/test-data/customers.json").stream()
                .map(node -> Arguments.of(
                        node.get("firstName").asText(),
                        node.get("lastName").asText(),
                        node.get("postCode").asText()
                ));
    }
}