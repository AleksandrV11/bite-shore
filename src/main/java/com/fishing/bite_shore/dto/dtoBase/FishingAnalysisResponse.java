package com.fishing.bite_shore.dto.dtoBase;

import java.time.LocalDate;

public record FishingAnalysisResponse(LocalDate fishingDate,
                                      String recommendation) {

}
