package com.fishing.bite_shore.weatherData.weather;

import jakarta.persistence.Embeddable;
import lombok.Data;
import org.springframework.stereotype.Component;

@Embeddable
@Data
public class Temperature {
    private int temperature;
}
