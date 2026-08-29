package com.fishing.bite_shore.service.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.dto.dtoBase.weather.DTOBase;
import com.fishing.bite_shore.service.gettingJSONData.Visualization;
import com.fishing.bite_shore.service.gettingJSONData.WeatherAPIClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeatherService {

    private final WeatherParser weatherParser;
    private final Visualization visualization;
    private final WeatherAPIClient weatherAPIClient;


    //повертає повноцінний обьєкт з даннимі (з назви)
    public DTOBase getParsDTOCity(String cityName) throws JsonProcessingException {
       // visualization.visualJSON(weatherAPIClient.getBaseJSONCity(cityName));//опціонально подивитись
        // джейсон
        return weatherParser.parsWeather(weatherAPIClient.getBaseJSONCity(cityName));
    }

    public DTOBase getParsDTOCoord(double lat, double lon) throws JsonProcessingException {
      //  visualization.visualJSON(weatherAPIClient.getBaseJSONCoord(lat, lon));//опціонально подивитись
        // джейсон
        return weatherParser.parsWeather(weatherAPIClient.getBaseJSONCoord(lat, lon));
    }


}
