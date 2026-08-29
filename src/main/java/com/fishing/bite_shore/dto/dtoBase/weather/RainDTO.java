package com.fishing.bite_shore.dto.dtoBase.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RainDTO {
    @JsonProperty("3h")
    private double threeH;
}
