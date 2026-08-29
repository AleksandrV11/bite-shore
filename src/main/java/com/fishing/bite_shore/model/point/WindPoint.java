package com.fishing.bite_shore.model.point;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WindPoint {

    private LocalDateTime dateTime;
    //  вітер
    private double windSpeed;
    //напрямок
    private int windDirection;
    //пориви
    private double windGust;
}
