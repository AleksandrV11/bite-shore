package com.fishing.bite_shore.dto.dtoBase;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rain {
    @JsonProperty("3h")
    private double threeH;
}
