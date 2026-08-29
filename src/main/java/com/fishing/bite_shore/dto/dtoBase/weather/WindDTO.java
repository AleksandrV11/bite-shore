package com.fishing.bite_shore.dto.dtoBase.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WindDTO {
    private double speed;
    private int deg;
    private double gust;
}
