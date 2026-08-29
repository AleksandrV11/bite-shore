package com.fishing.bite_shore.service.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishing.bite_shore.dto.dtoBase.weather.DTOBase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeatherParser {
    private final ObjectMapper objectMapper;


    public DTOBase parsWeather(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, DTOBase.class);
    }

}
