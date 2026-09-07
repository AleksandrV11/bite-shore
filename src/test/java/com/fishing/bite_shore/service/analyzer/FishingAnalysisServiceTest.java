package com.fishing.bite_shore.service.analyzer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fishing.bite_shore.dto.dtoBase.FishingAnalysisResponse;
import com.fishing.bite_shore.entity.WeatherLocationEntity;
import com.fishing.bite_shore.model.WeatherProcessResult;
import com.fishing.bite_shore.model.analysis.precipitation.PrecipitationState;
import com.fishing.bite_shore.model.analysis.pressure.PressureState;
import com.fishing.bite_shore.model.analysis.temperature.TemperatureState;
import com.fishing.bite_shore.model.analysis.wind.WindState;
import com.fishing.bite_shore.repository.WeatherLocationRepository;
import com.fishing.bite_shore.service.FishingAnalysisService;
import com.fishing.bite_shore.service.WeatherHistoryService;
import com.fishing.bite_shore.service.aggregator.PrecipitationAggregator;
import com.fishing.bite_shore.service.aggregator.PressureAggregator;
import com.fishing.bite_shore.service.aggregator.TemperatureAggregator;
import com.fishing.bite_shore.service.aggregator.WindAggregator;
import com.fishing.bite_shore.service.builder.DailyWeatherDataBuilder;
import com.fishing.bite_shore.service.orhestrator.WeatherOrchestratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FishingAnalysisServiceTest {
    @Mock
    private WeatherOrchestratorService weatherOrchestratorService;
    @Mock
    private WeatherHistoryService weatherHistoryService;
    @Mock
    private TemperatureAggregator temperatureAggregator;
    @Mock
    private PressureAggregator pressureAggregator;
    @Mock
    private WindAggregator windAggregator;
    @Mock
    private PrecipitationAggregator precipitationAggregator;
    @Mock
    private DailyWeatherDataBuilder dailyWeatherDataBuilder;
    @Mock
    private PressureAnalyzer pressureAnalyzer;
    @Mock
    private TemperatureAnalyzer temperatureAnalyzer;
    @Mock
    private WindAnalyzer windAnalyzer;
    @Mock
    private PrecipitationAnalyser precipitationAnalyser;
    @Mock
    private GeneralAnalyzer generalAnalyzer;
    @Mock
    private WeatherLocationRepository weatherLocationRepository;
    @InjectMocks
    private FishingAnalysisService fishingAnalysisService;
    private LocalDate localDate = LocalDate.now();

    //    Дата в минулому
    @Test
    void shouldThrowExceptionWhenDateIsInPast() throws JsonProcessingException {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fishingAnalysisService.analyze(
                        48.384756,
                        28.691357,
                        "Городківка",
                        localDate.minusDays(1)));
        assertEquals("Дата рибалки не може бути в минулому",exception.getMessage() );

    }

    //    Дата більше ніж 9 днів вперед
    @Test
    void shouldThrowExceptionWhenFishingDateIsMoreThanNineDaysAhead() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fishingAnalysisService.analyze(
                        48.384756,
                        28.691357,
                        "Городківка",
                        localDate.plusDays(10)));
        assertEquals("Дата рибалки не може бути більше ніж на 9 днів вперед",exception.getMessage() );
    }
// if (city == null || city.isBlank()) {
//        throw new IllegalArgumentException(
//                "Необхідно вказати місто, якщо координати не задані"
//        );
//    }
    //    Немає координат і міста
    @Test
    void shouldThrowExceptionWhenCoordinatesAndCityAreMissing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fishingAnalysisService.analyze(
                        null,
                        null,
                        null,
                        localDate.plusDays(3)));
        assertEquals("Необхідно вказати місто, якщо координати не задані",exception.getMessage() );
    }



    //    Координати не задані, але місто є — перевірити, що використовується getAndSaveByCity():
    @Test
    void shouldAnalyzeByCityWhenCoordinatesAreMissing() throws JsonProcessingException {

        WeatherProcessResult processResult = mock(WeatherProcessResult.class);

        when(weatherOrchestratorService.getAndSaveByCity("Городківка"))
                .thenReturn(processResult);

        when(processResult.getLocationId())
                .thenReturn(1L);

        when(weatherLocationRepository.findById(1L))
                .thenReturn(Optional.of(new WeatherLocationEntity()));

        when(weatherHistoryService.loadSnapshots(
                anyLong(),
                any(LocalDateTime.class)))
                .thenReturn(List.of());

        when(temperatureAggregator.agregator(anyList()))
                .thenReturn(List.of());

        when(pressureAggregator.agregator(anyList()))
                .thenReturn(List.of());

        when(windAggregator.agregator(anyList()))
                .thenReturn(List.of());

        when(precipitationAggregator.agregator(anyList()))
                .thenReturn(List.of());

        when(dailyWeatherDataBuilder.build(
                anyList(),
                anyList(),
                anyList(),
                anyList()))
                .thenReturn(List.of());

        when(pressureAnalyzer.analyze(
                anyList(),
                any(),
                any(LocalDate.class)))
                .thenReturn(new PressureState());

        when(temperatureAnalyzer.analyze(
                anyList(),
                any(LocalDate.class)))
                .thenReturn(new TemperatureState());

        when(windAnalyzer.analyze(
                anyList(),
                any(LocalDate.class)))
                .thenReturn(new WindState());

        when(precipitationAnalyser.analyze(
                anyList(),
                any(LocalDate.class)))
                .thenReturn(new PrecipitationState());

        when(generalAnalyzer.analyzer(
                any(),
                any(),
                any(),
                any(),
                any(LocalDateTime.class)))
                .thenReturn(new StringBuilder("Тестова рекомендація"));

        FishingAnalysisResponse response =
                fishingAnalysisService.analyze(
                        null,
                        null,
                        "Городківка",
                        localDate
                );

        assertNotNull(response);
        assertEquals(localDate, response.fishingDate());
        assertNotNull(response.recommendation());
        assertFalse(response.recommendation().isBlank());

        verify(weatherOrchestratorService)
                .getAndSaveByCity("Городківка");

        verify(weatherOrchestratorService, never())
                .getAndSaveByCoord(anyDouble(), anyDouble());
    }

}
