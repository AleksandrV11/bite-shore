package com.fishing.bite_shore.model.analysis.wind;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WindState {
    private int farPeriodDays;
    private int nearPeriodDays;
    private Wind farWind;
    private Wind nearWind;
}
