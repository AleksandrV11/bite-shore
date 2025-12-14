package com.fishing.bite_shore.weatherData.weather;

import jakarta.persistence.Embeddable;
import lombok.Data;
import org.mapstruct.Mapping;
@Embeddable
@Data
public class Coord {
    private double lat;
    private double lon;
}
