package com.fishing.bite_shore.model.analysis.precipitation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Precipitation {
    private int precipitation;
    private PrecipitationLevel level;
    private int popPercent;
}
