package com.fishing.bite_shore.model;

import com.fishing.bite_shore.dto.dtoBase.weather.DTOBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class WeatherProcessResult {
    private final DTOBase dtoBase;
    private final Long locationId;

}
