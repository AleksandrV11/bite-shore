package com.fishing.bite_shore.model;

import lombok.*;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class WeatherSnapshot {

    private LocalDateTime forecastTime;
    private double temperature;
    private int pressure;
    //вологість
    private int humidity;
    //  вітер
    private double windSpeed;
    private int windDirection;
    //пориви
    private double windGust;
    //опади кількість
    private int precipitation;
    //winListDto вірогідність опадів
    private int popPercent;

}
