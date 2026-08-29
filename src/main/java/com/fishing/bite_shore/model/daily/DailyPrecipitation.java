package com.fishing.bite_shore.model.daily;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyPrecipitation {
    private LocalDate localDate;
    //опади кількість
    private double maxPrecipitationDay;
    private double maxPrecipitationNight;
    //winListDto вірогідність опадів
    private int maxPopPercentDay;
    private int maxPopPercentNight;


}
