package com.fishing.bite_shore.service.gettingJSONData;

import com.fishing.bite_shore.dto.dtoBase.elevation.ElevationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class ElevationApiClient {
    private final WebClient webClient;

    public ElevationDTO getElevation(double lat, double lon) {
        try {
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

        } catch (WebClientResponseException e) {
            System.out.println("HTTP STATUS: " + e.getStatusCode());
            System.out.println("RESPONSE BODY: " + e.getResponseBodyAsString());
            throw e;
        }
    }
}
