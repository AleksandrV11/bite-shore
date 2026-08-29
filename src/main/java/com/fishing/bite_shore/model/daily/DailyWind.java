package com.fishing.bite_shore.model.daily;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyWind {
    private LocalDate date;
    //  вітер скорость +пориви
    private double effectiveWindSpeedDay;
    private double effectiveWindSpeedNight;
    //напрямок
    private Integer averageWindDirectionDay;
    private Integer averageWindDirectionNight;

}
