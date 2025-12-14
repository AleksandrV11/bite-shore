package com.fishing.bite_shore.servis.bd;

import com.fishing.bite_shore.entity.DayForecastEntity;
import com.fishing.bite_shore.entity.WeatherDataEntity;
import com.fishing.bite_shore.repository.DayForecastRepository;
import com.fishing.bite_shore.weatherData.DayForecast;
import com.fishing.bite_shore.weatherData.WeatherData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class WeatherServiceToBd {
    private final DayForecastRepository dayForecastRepository;

    public List<DayForecastEntity> mapToEntity(List<DayForecast> dayForecasts) {
        return dayForecasts.stream()
                .map(this::mapDayForecast).toList();
    }

    public void saveListDayForecastEntity(List<DayForecastEntity> dayForecastEntities) {

        dayForecastRepository.saveAll(dayForecastEntities);
    }

    private DayForecastEntity mapDayForecast(DayForecast dayForecast) {
        DayForecastEntity dayForecastEntity = new DayForecastEntity();
        dayForecastEntity.setCoord(dayForecast.getCoord());
        dayForecastEntity.setMorning(mapWeatherData(dayForecast.getMorning()));
        dayForecastEntity.setAfternoon(mapWeatherData(dayForecast.getAfternoon()));
        dayForecastEntity.setEvening(mapWeatherData(dayForecast.getEvening()));
        dayForecastEntity.setNight(mapWeatherData(dayForecast.getNight()));
        return dayForecastEntity;
    }

    private WeatherDataEntity mapWeatherData(WeatherData weatherData) {
        if (weatherData == null) {
            return null;
        }
        WeatherDataEntity weatherDataEntity = new WeatherDataEntity();
        weatherDataEntity.setTemperature(weatherData.getTemperature());
        weatherDataEntity.setLocalDateTime(weatherData.getLocalDateTime());
        weatherDataEntity.setAtmosphericPressure(weatherData.getAtmosphericPressure());
        weatherDataEntity.setRain(weatherData.getRain());
        weatherDataEntity.setWind(weatherData.getWind());
        return weatherDataEntity;
    }

}
