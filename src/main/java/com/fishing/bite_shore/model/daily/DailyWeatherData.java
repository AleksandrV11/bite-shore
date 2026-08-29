package com.fishing.bite_shore.model.daily;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeatherData {
    private LocalDate localDate;
    private DailyTemperature dailyTemperature;
    private DailyPressure dailyPressure;
    private DailyWind dailyWind;
    private DailyPrecipitation dailyPrecipitation;

}
