package com.fishing.bite_shore.dto.dtoBase.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WinListDTO {
    private long dt;
    private MainDTO main;
    private List<WeatherDTO> weather;
    private WindDTO wind;
    private int pop;
    private RainDTO rain;
    private String dt_txt;
}
