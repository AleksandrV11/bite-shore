package com.fishing.bite_shore.model.point;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PrecipitationPoint {
    private LocalDateTime dateTime;
    //опади кількість
    private int precipitation;
    //winListDto вірогідність опадів
    private int popPercent;
    //вологість
    private int humidity;
}
