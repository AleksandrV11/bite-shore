package com.fishing.bite_shore.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.dto.dtoBase.FishingAnalysisRequest;
import com.fishing.bite_shore.dto.dtoBase.FishingAnalysisResponse;
import com.fishing.bite_shore.service.FishingAnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fishing")
@RequiredArgsConstructor
public class FishingAnalysisController {
    private final FishingAnalysisService fishingAnalysisService;

    @PostMapping("/analyze")
    public FishingAnalysisResponse analyze(@Valid @RequestBody FishingAnalysisRequest request)
            throws JsonProcessingException {
        return fishingAnalysisService.analyze(
                request.latitude(),
                request.longitude(),
                request.city(),
                request.fishingDate());

    }
}
