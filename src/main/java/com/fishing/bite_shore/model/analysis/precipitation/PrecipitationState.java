package com.fishing.bite_shore.model.analysis.precipitation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrecipitationState {

    private Precipitation dayMinus2;
    private Precipitation dayMinus1;

}
