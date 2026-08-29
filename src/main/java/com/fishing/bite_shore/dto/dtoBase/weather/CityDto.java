package com.fishing.bite_shore.dto.dtoBase.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityDto {
    private int id;
    private String name;
    private CoordDTO coord;
    private String country;
    private long sunrise;
    private long sunset;
}
