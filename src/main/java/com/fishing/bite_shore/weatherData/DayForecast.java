package com.fishing.bite_shore.weatherData;

import com.fishing.bite_shore.dto.dtoBase.Coord;
import jakarta.persistence.Embedded;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class DayForecast {

    private Coord coord;
    private WeatherData morning;
    private WeatherData afternoon;
    private WeatherData evening;
    private WeatherData night;

}
