package com.fishing.bite_shore.servis.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishing.bite_shore.dto.dtoBase.DTO;
import org.springframework.stereotype.Service;

@Service
public class WeatherParser {
    private final ObjectMapper objectMapper;

    public WeatherParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DTO parsWeather(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, DTO.class);
    }

}
