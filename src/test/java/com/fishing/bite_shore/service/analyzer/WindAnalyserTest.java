package com.fishing.bite_shore.service.analyzer;

import com.fishing.bite_shore.exception.InvalidWindException;
import com.fishing.bite_shore.model.analysis.wind.Wind;
import com.fishing.bite_shore.model.analysis.wind.WindDirection;
import com.fishing.bite_shore.model.analysis.wind.WindState;
import com.fishing.bite_shore.model.analysis.wind.WindStrength;
import com.fishing.bite_shore.model.daily.DailyWeatherData;
import com.fishing.bite_shore.model.daily.DailyWind;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WindAnalyserTest {
    private final WindAnalyzer analyzer = new WindAnalyzer();
    private final LocalDate localDate = LocalDate.of(2026, 8, 26);

    @Test
    void shouldThrowExceptionWhenWindDataIsEmpty() {
        List<DailyWeatherData> weatherDataList = List.of();
        InvalidWindException exception = assertThrows(InvalidWindException.class,
                () -> analyzer.analyze(weatherDataList,
                        localDate));
        assertEquals("Немає даних по вітру", exception.getMessage());
    }

    @Test
    void shouldReturnNoDataWhenThereAreNoRelevantPeriods() {
        DailyWind wind = new DailyWind(
                localDate.minusDays(10),
                5,
                5,
                90,
                90
        );
        DailyWeatherData weatherData = new DailyWeatherData(
                localDate.minusDays(10),
                null,
                null,
                wind,
                null);
        WindState actual = analyzer.analyze(List.of(weatherData), localDate);

        assertEquals(0, actual.getFarWind().getStrenght());
        assertEquals(WindStrength.NO_DATA_STRENGHT,
                actual.getFarWind().getWindStrength());
        assertEquals(WindDirection.NO_DATA_DIRECTION,
                actual.getFarWind().getWindDirection());
        assertEquals(0, actual.getNearWind().getStrenght());
        assertEquals(WindStrength.NO_DATA_STRENGHT,
                actual.getNearWind().getWindStrength());
        assertEquals(WindDirection.NO_DATA_DIRECTION,
                actual.getNearWind().getWindDirection());
    }

    @Test
    void shouldAnalyzeNearPeriod() {
        DailyWind wind = new DailyWind(
                localDate.minusDays(1),
                4,
                6,
                90,
                90
        );

        DailyWeatherData weatherData = new DailyWeatherData(
                localDate.minusDays(1),
                null,
                null,
                wind,
                null
        );

        WindState actual = analyzer.analyze(
                List.of(weatherData),
                localDate
        );

        assertEquals(0, actual.getFarWind().getStrenght());
        assertEquals(WindStrength.NO_DATA_STRENGHT,
                actual.getFarWind().getWindStrength());
        assertEquals(WindDirection.NO_DATA_DIRECTION,
                actual.getFarWind().getWindDirection());

        assertEquals(5, actual.getNearWind().getStrenght());
        assertEquals(WindStrength.MODERATE,
                actual.getNearWind().getWindStrength());
        assertEquals(WindDirection.E,
                actual.getNearWind().getWindDirection());
    }

    @Test
    void shouldAnalyzeFarPeriod() {

        DailyWind wind = new DailyWind(
                localDate.minusDays(3),
                4,
                6,
                90,
                90
        );

        DailyWeatherData weatherData = new DailyWeatherData(
                localDate.minusDays(3),
                null,
                null,
                wind,
                null
        );
        WindState actual = analyzer.analyze(
                List.of(weatherData),
                localDate
        );
        assertEquals(5, actual.getFarWind().getStrenght());
        assertEquals(WindStrength.MODERATE,
                actual.getFarWind().getWindStrength());
        assertEquals(WindDirection.E,
                actual.getFarWind().getWindDirection());

        assertEquals(0, actual.getNearWind().getStrenght());
        assertEquals(WindStrength.NO_DATA_STRENGHT,
                actual.getNearWind().getWindStrength());
        assertEquals(WindDirection.NO_DATA_DIRECTION,
                actual.getNearWind().getWindDirection());
    }

    @Test
    void shouldAnalyzeFarAndNearPeriods() {
        DailyWind farWind = new DailyWind(
                localDate.minusDays(3),
                4,
                6,
                90,
                90
        );

        DailyWind nearWind = new DailyWind(
                localDate.minusDays(1),
                6,
                8,
                180,
                180
        );

        DailyWeatherData farData = new DailyWeatherData(
                localDate.minusDays(3),
                null,
                null,
                farWind,
                null
        );

        DailyWeatherData nearData = new DailyWeatherData(
                localDate.minusDays(1),
                null,
                null,
                nearWind,
                null
        );

        WindState actual = analyzer.analyze(
                List.of(farData, nearData),
                localDate
        );

        assertEquals(5, actual.getFarWind().getStrenght());
        assertEquals(WindStrength.MODERATE,
                actual.getFarWind().getWindStrength());
        assertEquals(WindDirection.E,
                actual.getFarWind().getWindDirection());

        assertEquals(7, actual.getNearWind().getStrenght());
        assertEquals(WindStrength.STRONG,
                actual.getNearWind().getWindStrength());
        assertEquals(WindDirection.S,
                actual.getNearWind().getWindDirection());
    }

    @Test
    void shouldIgnoreDataOutsideAnalysisPeriods() {
        DailyWind wind = new DailyWind(
                localDate.minusDays(10),
                20,
                20,
                180,
                180
        );

        DailyWeatherData weatherData = new DailyWeatherData(
                localDate.minusDays(10),
                null,
                null,
                wind,
                null
        );

        WindState actual = analyzer.analyze(
                List.of(weatherData),
                localDate
        );

        assertEquals(0, actual.getFarWind().getStrenght());
        assertEquals(WindStrength.NO_DATA_STRENGHT,
                actual.getFarWind().getWindStrength());

        assertEquals(0, actual.getNearWind().getStrenght());
        assertEquals(WindStrength.NO_DATA_STRENGHT,
                actual.getNearWind().getWindStrength());
    }


    @Test
    void shouldCalculateAverageEffectiveWindSpeed() {
        DailyWind wind = new DailyWind(
                localDate.minusDays(1),
                4,
                8,
                90,
                90
        );

        DailyWeatherData weatherData = new DailyWeatherData(
                localDate.minusDays(1),
                null,
                null,
                wind,
                null
        );

        WindState actual = analyzer.analyze(
                List.of(weatherData),
                localDate
        );

        assertEquals(6, actual.getNearWind().getStrenght());
    }
    @Test
    void shouldCalculateAverageWindSpeedForSeveralDays() {
        DailyWind wind1 = new DailyWind(
                localDate.minusDays(3),
                4,
                4,
                90,
                90
        );

        DailyWind wind2 = new DailyWind(
                localDate.minusDays(2),
                8,
                8,
                90,
                90
        );

        DailyWeatherData data1 = new DailyWeatherData(
                localDate.minusDays(3),
                null,
                null,
                wind1,
                null
        );

        DailyWeatherData data2 = new DailyWeatherData(
                localDate.minusDays(2),
                null,
                null,
                wind2,
                null
        );

        WindState actual = analyzer.analyze(
                List.of(data1, data2),
                localDate
        );

        assertEquals(6, actual.getFarWind().getStrenght());
        assertEquals(WindStrength.STRONG,
                actual.getFarWind().getWindStrength());
    }


    @Test
    void shouldDetermineWindStrengthAtBoundaries() {
        DailyWind wind = new DailyWind(
                localDate.minusDays(1),
                2,
                2,
                90,
                90
        );

        DailyWeatherData weatherData = new DailyWeatherData(
                localDate.minusDays(1),
                null,
                null,
                wind,
                null
        );

        WindState actual = analyzer.analyze(
                List.of(weatherData),
                localDate
        );

        assertEquals(
                WindStrength.LIGHT,
                actual.getNearWind().getWindStrength()
        );
    }


    @Test
    void shouldCalculateAverageDayAndNightDirection() {
        DailyWind wind = new DailyWind(
                localDate.minusDays(1),
                4,
                4,
                0,
                90
        );

        DailyWeatherData weatherData = new DailyWeatherData(
                localDate.minusDays(1),
                null,
                null,
                wind,
                null
        );

        WindState actual = analyzer.analyze(
                List.of(weatherData),
                localDate
        );

        assertEquals(
                WindDirection.NE,
                actual.getNearWind().getWindDirection()
        );
    }

    @Test
    void shouldCorrectlyCalculateDirectionAroundZeroDegrees() {
        DailyWind wind = new DailyWind(
                localDate.minusDays(1),
                4,
                4,
                350,
                10
        );

        DailyWeatherData weatherData = new DailyWeatherData(
                localDate.minusDays(1),
                null,
                null,
                wind,
                null
        );

        WindState actual = analyzer.analyze(
                List.of(weatherData),
                localDate
        );

        assertEquals(
                WindDirection.N,
                actual.getNearWind().getWindDirection()
        );
    }

    @Test
    void shouldHandleNullWindDirections() {
        DailyWind wind = new DailyWind(
                localDate.minusDays(1),
                4,
                4,
                null,
                90
        );

        DailyWeatherData weatherData = new DailyWeatherData(
                localDate.minusDays(1),
                null,
                null,
                wind,
                null
        );

        WindState actual = analyzer.analyze(
                List.of(weatherData),
                localDate
        );

        assertEquals(
                WindDirection.E,
                actual.getNearWind().getWindDirection()
        );

        DailyWind bothNullWind = new DailyWind(
                localDate.minusDays(1),
                4,
                4,
                null,
                null
        );

        DailyWeatherData bothNullData = new DailyWeatherData(
                localDate.minusDays(1),
                null,
                null,
                bothNullWind,
                null
        );

        actual = analyzer.analyze(
                List.of(bothNullData),
                localDate
        );

        assertEquals(
                WindDirection.NO_DATA_DIRECTION,
                actual.getNearWind().getWindDirection()
        );
    }

}
