package com.fishing.bite_shore.service.gettingJSONData;

import com.fishing.bite_shore.dto.dtoBase.elevation.ElevationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ElevationApiClient {
    private final WebClient webClient;

    public ElevationDTO getElevation(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.open-meteo.com")
                        .path("/v1/elevation")
                        .queryParam("latitude", lat)
                        .queryParam("longitude", lon)
                        .build())
                .retrieve()
                .bodyToMono(ElevationDTO.class)
                .block();
    }
}
