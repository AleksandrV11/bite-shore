package com.fishing.bite_shore.model.analysis.temperature;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Temperature {
    private WaterLayerState upperLayerState;
    private WaterLayerState middleLayerState;
}
