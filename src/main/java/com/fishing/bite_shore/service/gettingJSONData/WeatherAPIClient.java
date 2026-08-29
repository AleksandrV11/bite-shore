package com.fishing.bite_shore.service.gettingJSONData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WeatherAPIClient {

    private final WebClient webClient;

    @Value("${api.key}")
    private String apiKey="d80602614ec5215d65ff2ceda32e44b1";

    private static final String URL_BY_CITY =
            "https://api.openweathermap.org/data/2.5/forecast?q={city}&appid={apiKey}&units=metric&lang=ua";
    private static final String URL_BY_COORD =
            "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={apiKey}&units=metric&lang=ua";


    public String getBaseJSONCity(String city) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/forecast")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", "ua")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getBaseJSONCoord(double lat, double lon) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/forecast")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", "ua")
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}











