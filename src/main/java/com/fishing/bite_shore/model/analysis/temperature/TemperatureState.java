package com.fishing.bite_shore.model.analysis.temperature;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemperatureState {
    private int farPeriodDays;
    private int nearPeriodDays;
    private Temperature temperatureFarPeriod;
    private Temperature temperatureNearPeriod;

}
