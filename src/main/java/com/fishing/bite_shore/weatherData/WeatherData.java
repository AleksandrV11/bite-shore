package com.fishing.bite_shore.weatherData;

import com.fishing.bite_shore.dto.dtoBase.Coord;
import com.fishing.bite_shore.weatherData.weather.AtmosphericPressure;
import com.fishing.bite_shore.weatherData.weather.Rain;
import com.fishing.bite_shore.weatherData.weather.Temperature;
import com.fishing.bite_shore.weatherData.weather.Wind;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeatherData {
    private LocalDateTime localDateTime;
    private AtmosphericPressure atmosphericPressure;
    private Rain rain;
    private Temperature temperature;
    private Wind wind;

}
