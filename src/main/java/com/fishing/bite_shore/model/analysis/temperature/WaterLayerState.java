package com.fishing.bite_shore.model.analysis.temperature;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WaterLayerState {
    private int headTemp;
    private HeatLevel heatLevel;// Оцінка прогріву шару води. COLD / COOL / COMFORT / WARM / HOT
    private int headTrend;
    private WaterTrend temperatureTrend;    //  двіж

}
