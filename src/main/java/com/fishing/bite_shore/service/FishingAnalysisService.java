package com.fishing.bite_shore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.dto.dtoBase.FishingAnalysisResponse;
import com.fishing.bite_shore.entity.WeatherLocationEntity;
import com.fishing.bite_shore.model.WeatherProcessResult;
import com.fishing.bite_shore.model.WeatherSnapshot;
import com.fishing.bite_shore.model.analysis.precipitation.PrecipitationState;
import com.fishing.bite_shore.model.analysis.pressure.PressureState;
import com.fishing.bite_shore.model.analysis.temperature.TemperatureState;
import com.fishing.bite_shore.model.analysis.wind.WindState;
import com.fishing.bite_shore.model.daily.*;
import com.fishing.bite_shore.repository.WeatherLocationRepository;
import com.fishing.bite_shore.service.aggregator.PrecipitationAggregator;
import com.fishing.bite_shore.service.aggregator.PressureAggregator;
import com.fishing.bite_shore.service.aggregator.TemperatureAggregator;
import com.fishing.bite_shore.service.aggregator.WindAggregator;
import com.fishing.bite_shore.service.analyzer.*;
import com.fishing.bite_shore.service.builder.DailyWeatherDataBuilder;
import com.fishing.bite_shore.service.orhestrator.WeatherOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FishingAnalysisService {
    private final WeatherOrchestratorService weatherOrchestratorService;
    private final WeatherHistoryService weatherHistoryService;
    private final TemperatureAggregator temperatureAggregator;
    private final PressureAggregator pressureAggregator;
    private final WindAggregator windAggregator;
    private final PrecipitationAggregator precipitationAggregator;
    private final DailyWeatherDataBuilder dailyWeatherDataBuilder;
    private final PressureAnalyzer pressureAnalyzer;
    private final TemperatureAnalyzer temperatureAnalyzer;
    private final WindAnalyzer windAnalyzer;
    private final PrecipitationAnalyser precipitationAnalyser;
    private final GeneralAnalyzer generalAnalyzer;
    private final WeatherLocationRepository weatherLocationRepository;


    public FishingAnalysisResponse analyze(Double lat, Double lon, String city, LocalDate localDate) throws JsonProcessingException {

        LocalDateTime dateTime = localDate.atStartOfDay();

        if (localDate.isAfter(LocalDate.now().plusDays(9))) {
            throw new IllegalArgumentException(
                    "Дата рибалки не може бути більше ніж на 9 днів вперед"
            );
        }
        if (localDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Дата рибалки не може бути в минулому"
            );
        }
        WeatherProcessResult processResult;
        if (lat == null || lon == null || lat == 0 || lon == 0) {

            if (city == null || city.isBlank()) {
                throw new IllegalArgumentException(
                        "Необхідно вказати місто, якщо координати не задані"
                );
            }
            processResult = weatherOrchestratorService.getAndSaveByCity(city);
        } else {
            processResult = weatherOrchestratorService.getAndSaveByCoord(lat, lon);
        }
        //
        // DTOBase dto =orchestratorService.getAndSaveByCity(nameCity);
        //по коордінатах

        // Отримуємо висоту місцевості
        WeatherLocationEntity weatherLocation = weatherLocationRepository
                .findById(processResult.getLocationId())
                .orElseThrow();
        // Завантажуємо історію погоди
        List<WeatherSnapshot> snapshotList =
                weatherHistoryService.loadSnapshots(
                        processResult.getLocationId(),
                        dateTime
                );
        // Агрегуємо дані
        List<DailyTemperature> dailyTemperatures =
                temperatureAggregator.agregator(snapshotList);
        List<DailyPressure> dailyPressures =
                pressureAggregator.agregator(snapshotList);
        List<DailyWind> dailyWinds =
                windAggregator.agregator(snapshotList);
        List<DailyPrecipitation> dailyPrecipitations =
                precipitationAggregator.agregator(snapshotList);
        // Об'єднуємо все в DailyWeatherData
        List<DailyWeatherData> dailyWeatherData =
                dailyWeatherDataBuilder.build(
                        dailyTemperatures,
                        dailyPressures,
                        dailyWinds,
                        dailyPrecipitations
                );
        // Аналіз
        PressureState pressure =
                pressureAnalyzer.analyze(
                        dailyWeatherData,
                        weatherLocation.getElevation(),
                        localDate
                );
        TemperatureState temperature =
                temperatureAnalyzer.analyze(
                        dailyWeatherData,
                        localDate
                );
        WindState wind =
                windAnalyzer.analyze(
                        dailyWeatherData,
                        localDate
                );
        PrecipitationState precipitation =
                precipitationAnalyser.analyze(
                        dailyWeatherData,
                        localDate
                );
        // Загальний аналіз
        String result =
                (generalAnalyzer.analyzer(
                        pressure,
                        temperature,
                        wind,
                        precipitation,
                        dateTime
                )).toString();
        return new FishingAnalysisResponse(localDate, result);

    }
}
