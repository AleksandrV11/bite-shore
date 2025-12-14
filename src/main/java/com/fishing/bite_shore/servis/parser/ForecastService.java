package com.fishing.bite_shore.servis.parser;

import com.fishing.bite_shore.dto.dtoBase.Coord;
import com.fishing.bite_shore.weatherData.DayForecast;
import com.fishing.bite_shore.weatherData.WeatherData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForecastService {

    public List<DayForecast> getDayForecastList(List<WeatherData> weatherDataList, double lat, double lon) {
        Map<LocalDate, List<WeatherData>> dayForecastMap =
                weatherDataList.stream().collect(Collectors.groupingBy(w -> w.getLocalDateTime().toLocalDate()));
        List<DayForecast> dayForecastList = dayForecastMap.values().stream().map(a -> {
            DayForecast dayForecast = new DayForecast();
            dayForecast.setCoord(new Coord()); // Додаємо цю строку!
            dayForecast.getCoord().setLat(lat);
            dayForecast.getCoord().setLon(lon);
            for (WeatherData weatherData : a) {
                int hour = weatherData.getLocalDateTime().getHour();
                if (hour == 6) {
                    dayForecast.setMorning(weatherData);
                } else if (hour == 12) {
                    dayForecast.setAfternoon(weatherData);
                } else if (hour == 18) {
                    dayForecast.setEvening(weatherData);
                } else if (hour == 0) {
                    dayForecast.setNight(weatherData);
                }
            }
            return dayForecast;
        }).toList();
        return dayForecastList;
    }

}


