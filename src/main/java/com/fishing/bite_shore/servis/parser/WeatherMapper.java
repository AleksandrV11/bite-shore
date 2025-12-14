package com.fishing.bite_shore.servis.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.dto.dtoBase.City;
import com.fishing.bite_shore.dto.dtoBase.Coord;
import com.fishing.bite_shore.dto.dtoBase.DTO;
import com.fishing.bite_shore.dto.dtoBase.WinList;
import com.fishing.bite_shore.mapper.WeatherDataMaper;
import com.fishing.bite_shore.weatherData.WeatherData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherMapper {
    private final WeatherDataMaper weatherDataMaper;
    private final WeatherService weatherService;

    public WeatherData mapWeatherData(WinList winList) {
        return weatherDataMaper.toWeatherData(winList);
    }

    public List<WinList> getWinList(String nameCity) throws JsonProcessingException {
        DTO dto = weatherService.getParsDTOCity(nameCity);
        return dto.getList();
    }
    public List<WinList> getWinListCoord(double lat,double lon) throws JsonProcessingException {
        DTO dto = weatherService.getParsDTOCoord(lat,lon);
        return dto.getList();
    }

    public List<WeatherData> mapWinListList(List<WinList> winLists) {
        return winLists.stream()
                .map(weatherDataMaper::toWeatherData)
                .toList();
    }

}
