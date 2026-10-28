package com.fishing.bite_shore.dto.dtoBase;

import java.time.LocalDate;
import java.util.List;

public record FishingAnalysisResponse(LocalDate fishingDate,
                                      List<String> recommendation) {

}
