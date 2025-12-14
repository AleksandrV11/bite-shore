package com.fishing.bite_shore.servis.gettingJSONData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherAPIClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String API_KEY = "a003e384b7da83eaa6099f80612a0c32";
    //пошук по назві міста
    //    private static final String URL =
//            "https://api.openweathermap.org/data/2.5/forecast?q={city}&appid={apiKey}&units=metric&lang=ua";
    // пошук по координатам
    private static final String URL =
            "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={apiKey}&units=metric&lang=ua";

    public WeatherAPIClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getBaseJSONCity(String nameCity) {
        return restTemplate.getForObject(URL, String.class, nameCity, API_KEY);
    }

    public String getBaseJSONCoord(double lat, double lon) {
        Map<String, Object> mapCoord = new HashMap<>();
        mapCoord.put("lat", lat);
        mapCoord.put("lon", lon);
        mapCoord.put("apiKey",API_KEY);
        return restTemplate.getForObject(URL, String.class, mapCoord);
    }

    public String getFilteredJSON(String nameCity) throws JsonProcessingException {
        String json = getBaseJSONCity(nameCity);
        Map<String, Object> mapFilteredObject = objectMapper.readValue(json, new TypeReference<>() {
        });
        List<Object> objectList = (List<Object>) mapFilteredObject.get("list");
        List<Object> objectsFilterList = objectList.stream()
                .filter(a -> {
                    Map<String, Object> mapFild = (Map<String, Object>) a;
                    String date = (String) mapFild.get("dt_txt");
                    return date.endsWith("00:00:00") || date.endsWith("06:00:00")
                            || date.endsWith("12:00:00") || date.endsWith("18:00:00");
                }).toList();
        mapFilteredObject.put("list", objectsFilterList);
        return objectMapper.writeValueAsString(mapFilteredObject);
    }

    public String getFilteredJSONCoord(double lat, double lon) throws JsonProcessingException {
        String json = getBaseJSONCoord(lat, lon);
        Map<String, Object> mapFilteredObject = objectMapper.readValue(json, new TypeReference<>() {
        });
        List<Object> objectList = (List<Object>) mapFilteredObject.get("list");
        List<Object> objectsFilterList = objectList.stream()
                .filter(a -> {
                    Map<String, Object> mapFild = (Map<String, Object>) a;
                    String date = (String) mapFild.get("dt_txt");
                    return date.endsWith("00:00:00") || date.endsWith("06:00:00")
                            || date.endsWith("12:00:00") || date.endsWith("18:00:00");
                }).toList();
        mapFilteredObject.put("list", objectsFilterList);
        return objectMapper.writeValueAsString(mapFilteredObject);
    }

}











