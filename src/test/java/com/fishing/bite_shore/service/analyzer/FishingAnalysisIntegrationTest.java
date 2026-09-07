package com.fishing.bite_shore.service.analyzer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.dto.dtoBase.FishingAnalysisResponse;
import com.fishing.bite_shore.service.FishingAnalysisService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FishingAnalysisIntegrationTest {
    private final FishingAnalysisService fishingAnalysisService;
    private final LocalDate localDate = LocalDate.now().plusDays(3);

    @Test
    void shouldAnalyzeFishingConditions() throws JsonProcessingException {
        FishingAnalysisResponse response = fishingAnalysisService.analyze(
                48.384756,
                28.691357,
                "Городківка",
                localDate);
        assertNotNull(response);
        assertEquals(localDate, response.fishingDate());
        assertNotNull(response.recommendation());
        assertFalse(response.recommendation().isBlank());
    }

    @Test
    void shouldAnalyzeFishingConditionsByCity() throws JsonProcessingException {
        FishingAnalysisResponse response = fishingAnalysisService.analyze(
                null,
                null,
                "Городківка",
                localDate);
        assertNotNull(response);
        assertEquals(localDate, response.fishingDate());
        assertNotNull(response.recommendation());
        assertFalse(response.recommendation().isBlank());
    }
}
