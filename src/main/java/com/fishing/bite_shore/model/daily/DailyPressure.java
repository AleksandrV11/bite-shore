package com.fishing.bite_shore.model.daily;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyPressure {
    private LocalDate localDate;
    private Integer averagePressureDay;
    private Integer averagePressureNight;
}

