package com.fishing.bite_shore.model.analysis.precipitation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrecipitationState {

    private Precipitation dayMinus2;
    private Precipitation dayMinus1;

}
