package com.fishing.bite_shore.entity;


import com.fishing.bite_shore.dto.dtoBase.Coord;
import com.fishing.bite_shore.weatherData.WeatherData;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "day_forecast")
public class DayForecastEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Coord coord;

    @OneToOne(cascade = CascadeType.ALL)
    private WeatherDataEntity morning;

    @OneToOne(cascade = CascadeType.ALL)
    private WeatherDataEntity afternoon;

    @OneToOne(cascade = CascadeType.ALL)
    private WeatherDataEntity evening;

    @OneToOne(cascade = CascadeType.ALL)
    private WeatherDataEntity night;

}

