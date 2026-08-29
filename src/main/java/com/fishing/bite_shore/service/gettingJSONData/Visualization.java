package com.fishing.bite_shore.service.gettingJSONData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fishing.bite_shore.dto.dtoBase.weather.DTOBase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Visualization {

    private final ObjectMapper objectMapper;

    public void visualJSON(String json) throws JsonProcessingException {
        Object objectJSON = objectMapper.readValue(json, Object.class);
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        System.out.println(objectWriter.writeValueAsString(objectJSON));
    }

    public void visualDTOBase(DTOBase dtoBase){
        try {
            System.out.println(objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(dtoBase));
        } catch (Exception e) {
            throw new RuntimeException("Visualization DTOBase failed", e);
        }
    }

}
