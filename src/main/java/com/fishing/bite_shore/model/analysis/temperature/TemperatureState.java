package com.fishing.bite_shore.model.analysis.temperature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureState {
    private int farPeriodDays;
    private int nearPeriodDays;
    private Temperature temperatureFarPeriod;
    private Temperature temperatureNearPeriod;

}
