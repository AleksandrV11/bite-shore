package com.fishing.bite_shore.model.point;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TemperaturePoint {
    private LocalDateTime dateTime;
    private double temperature;
}
