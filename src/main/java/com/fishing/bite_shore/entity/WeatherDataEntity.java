package com.fishing.bite_shore.entity;

import com.fishing.bite_shore.dto.dtoBase.Coord;
import com.fishing.bite_shore.weatherData.weather.AtmosphericPressure;
import com.fishing.bite_shore.weatherData.weather.Rain;
import com.fishing.bite_shore.weatherData.weather.Temperature;
import com.fishing.bite_shore.weatherData.weather.Wind;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
//@Table(name = "weather_data", uniqueConstraints = @UniqueConstraint(columnNames = {"local_date_time"}))
@Table(name = "weather_data")
public class WeatherDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime localDateTime;
    @Embedded
    private AtmosphericPressure atmosphericPressure;
    @Embedded
    private Rain rain;
    @Embedded
    private Temperature temperature;
    @Embedded
    private Wind wind;
}
