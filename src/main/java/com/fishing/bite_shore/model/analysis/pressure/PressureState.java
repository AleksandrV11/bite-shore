package com.fishing.bite_shore.model.analysis.pressure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PressureState {

    private int startPressure;
    private int endPressure;
    private int normPressure;
    //який зараз тиск низький норм та висок
    private PressureLevel pressureLevel;
    // напрям руху падає стаб зростає
    private PressureMovement pressureMovement;
   // останны тренд
    private PressureTrend pressureTrend;
}
