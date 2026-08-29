package com.fishing.bite_shore.service.builder;

import com.fishing.bite_shore.model.daily.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DailyWeatherDataBuilder {
    public List<DailyWeatherData> build(
            List<DailyTemperature> temperatures
            , List<DailyPressure> pressures
            , List<DailyWind> winds
            , List<DailyPrecipitation> precipitations) {
        Map<LocalDate, DailyTemperature> temperatureMap = temperatures.stream()
                .collect(Collectors.toMap(DailyTemperature::getLocalDate, Function.identity()));
        Map<LocalDate, DailyPressure> pressureMap = pressures.stream()
                .collect(Collectors.toMap(DailyPressure::getLocalDate, Function.identity()));
        Map<LocalDate, DailyWind> windMap = winds.stream()
                .collect(Collectors.toMap(DailyWind::getDate, Function.identity()));
        Map<LocalDate, DailyPrecipitation> precipitationMap = precipitations.stream()
                .collect(Collectors.toMap(DailyPrecipitation::getLocalDate, Function.identity()));
        return temperatureMap.keySet().stream()
                .sorted()
                .map(date -> new DailyWeatherData(
                        date,
                        temperatureMap.get(date)
                        , pressureMap.get(date)
                        , windMap.get(date)
                        , precipitationMap.get(date)
                )).toList();
    }
}
