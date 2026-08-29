package com.fishing.bite_shore.dto.dtoBase.elevation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElevationDTO {
    private List<Double> elevation;

    public Double getElevationValue() {
        return !elevation.isEmpty() && elevation != null ? elevation.get(0) : null;
    }
}
