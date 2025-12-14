package com.fishing.bite_shore.weatherData.weather;

import jakarta.persistence.Embeddable;
import lombok.Data;
import org.springframework.stereotype.Component;
@Embeddable
@Data
public class Wind {
    private int windDirection;
    private double windPower;
    private double impulseForce;
}
