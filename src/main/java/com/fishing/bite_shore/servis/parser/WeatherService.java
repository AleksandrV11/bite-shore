package com.fishing.bite_shore.servis.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.dto.dtoBase.DTO;
import com.fishing.bite_shore.servis.gettingJSONData.VisualizationJSON;
import com.fishing.bite_shore.servis.gettingJSONData.WeatherAPIClient;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    private final WeatherAPIClient weatherAPIClient;
    private final WeatherParser weatherParser;
    private final VisualizationJSON visualizationJSON;

    public WeatherService(WeatherAPIClient weatherAPIClient, WeatherParser weatherParser, VisualizationJSON visualizationJSON) {
        this.weatherAPIClient = weatherAPIClient;
        this.weatherParser = weatherParser;
        this.visualizationJSON = visualizationJSON;
    }

    //повертає повноцінний обьєкт з даннимі (з назви)
    public DTO getParsDTOCity(String nameCiti) throws JsonProcessingException {
        String json = weatherAPIClient.getFilteredJSON(nameCiti); //по назві
        //  System.out.println(visualizationJSON.getVisualJSON(json));//опціонально подивитись джейсон
        return weatherParser.parsWeather(json);
    }

    //повертає повноцінний обьєкт з даннимі (з координат)
    public DTO getParsDTOCoord(double lat, double lon) throws JsonProcessingException {
        //   String json = weatherAPIClient.getFilteredJSON(nameCiti); по назві
        String json = weatherAPIClient.getFilteredJSONCoord(lat, lon); //по координатам
        System.out.println(visualizationJSON.getVisualJSON(json));//опціонально подивитись джейсон
        return weatherParser.parsWeather(json);
    }


}
