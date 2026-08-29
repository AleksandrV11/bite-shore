package com.fishing.bite_shore.model.daily;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyTemperature {
    private LocalDate localDate;
    private Double averageTemperatureDay;
    private Double averageTemperatureNight;
}
