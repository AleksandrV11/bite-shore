package com.fishing.bite_shore.model.analysis.wind;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Wind {

    private int strenght;
    private WindStrength windStrength;
    private WindDirection windDirection;
}
