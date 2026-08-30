package com.fishing.bite_shore.dto.dtoBase;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FishingAnalysisRequest(


        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Double latitude,


        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Double longitude,

        String city,

        @NotNull
        LocalDate fishingDate
) {
}