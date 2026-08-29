package com.fishing.bite_shore.dto.dtoBase.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDTO {
    private int id;
    private String main;
    private String description;
    private String icon;
}
