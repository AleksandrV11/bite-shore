package com.fishing.bite_shore.servis.gettingJSONData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.stereotype.Service;

@Service
public class VisualizationJSON {
    private final ObjectMapper objectMapper;

    public VisualizationJSON(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getVisualJSON(String json) throws JsonProcessingException {
        Object objectJSON = objectMapper.readValue(json, Object.class);
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(objectJSON);
    }

}
