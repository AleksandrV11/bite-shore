package com.fishing.bite_shore.model.analysis.wind;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WindState {
    private int farPeriodDays;
    private int nearPeriodDays;
    private Wind farWind;
    private Wind nearWind;
}
