package com.fishing.bite_shore.service.analyzer;

import com.fishing.bite_shore.exception.InvalidPressureException;
import com.fishing.bite_shore.exception.InvalidTemperatureException;
import com.fishing.bite_shore.model.analysis.pressure.PressureLevel;
import com.fishing.bite_shore.model.analysis.pressure.PressureMovement;
import com.fishing.bite_shore.model.analysis.pressure.PressureState;
import com.fishing.bite_shore.model.analysis.pressure.PressureTrend;
import com.fishing.bite_shore.model.analysis.temperature.*;
import com.fishing.bite_shore.model.daily.DailyPressure;
import com.fishing.bite_shore.model.daily.DailyTemperature;
import com.fishing.bite_shore.model.daily.DailyWeatherData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TemperatureAnalyserTest {

    TemperatureAnalyzer analyser = new TemperatureAnalyzer();

    @Test
    void shouldReturnEmptyStateWhenTemperatureDataIsMissing() {
        List<DailyWeatherData> dailyWeatherData = List.of();
        TemperatureState expected = new TemperatureState(0,
                0,
                null,
                null);
        TemperatureState actual = analyser.analyze(dailyWeatherData,
                LocalDate.of(2026, 8, 24));
        assertEquals(expected, actual);
    }

    @Test
    void shouldAnalyzeFarPeriodWhenOnlyFarPeriodDataExists() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate farDate = localDate.minusDays(2);
        DailyTemperature dailyTemperature = new DailyTemperature(farDate,
                25.0,
                25.3);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(
                farDate,
                dailyTemperature,
                null,
                null,
                null);
        Temperature expectedTemperature = new Temperature(
                new WaterLayerState(24, HeatLevel.COMFORT, 0, WaterTrend.STABLE),
                new WaterLayerState(19, HeatLevel.COMFORT, 0, WaterTrend.STABLE)
        );
        TemperatureState expected = new TemperatureState(1,
                0,
                expectedTemperature,
                null);
        TemperatureState actual = analyser.analyze(
                List.of(dailyWeatherData),
                localDate
        );
        assertEquals(expected, actual);
    }

    @Test
    void shouldAnalyzeNearPeriodWhenOnlyNearPeriodDataExists() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate nearDate = localDate.minusDays(1);
        DailyTemperature dailyTemperature = new DailyTemperature(nearDate,
                25.0,
                25.3);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(
                nearDate,
                dailyTemperature,
                null,
                null,
                null);
        Temperature expectedTemperature = new Temperature(
                new WaterLayerState(24, HeatLevel.COMFORT, 0, WaterTrend.STABLE),
                new WaterLayerState(19, HeatLevel.COMFORT, 0, WaterTrend.STABLE)
        );
        TemperatureState expected = new TemperatureState(
                0,
                1,
                null,
                expectedTemperature);
        TemperatureState actual = analyser.analyze(
                List.of(dailyWeatherData),
                localDate
        );
        assertEquals(expected, actual);
    }

    @Test
    void shouldAnalyzeBothFarAndNearPeriods() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate farDate = localDate.minusDays(2);
        LocalDate nearDate = localDate.minusDays(1);
        DailyTemperature nearTemperature = new DailyTemperature(nearDate,
                25.0,
                26.0);
        DailyTemperature farTemperature = new DailyTemperature(farDate,
                25.0,
                26.0);
        DailyWeatherData farWeatherData = new DailyWeatherData(
                farDate,
                farTemperature,
                null,
                null,
                null);
        DailyWeatherData nearWeatherData = new DailyWeatherData(
                nearDate,
                nearTemperature,
                null,
                null,
                null);

        Temperature expectedFarTemperature = new Temperature(
                new WaterLayerState(25, HeatLevel.COMFORT, 0, WaterTrend.STABLE),
                new WaterLayerState(20, HeatLevel.COMFORT, 0, WaterTrend.STABLE)
        );
        Temperature expectedNearTemperature = new Temperature(
                new WaterLayerState(25, HeatLevel.COMFORT, 0, WaterTrend.STABLE),
                new WaterLayerState(20, HeatLevel.COMFORT, 0, WaterTrend.STABLE)
        );
        TemperatureState expected = new TemperatureState(
                1,
                1,
                expectedFarTemperature,
                expectedNearTemperature);
        TemperatureState actual = analyser.analyze(
                List.of(farWeatherData, nearWeatherData),
                localDate
        );
        assertEquals(expected, actual);
    }

    @Test
    void shouldUseDayTemperatureWhenNightTemperatureIsMissing() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate nearDate = localDate.minusDays(1);
        DailyTemperature dailyTemperature = new DailyTemperature(nearDate,
                25.0,
                null);
        DailyWeatherData weatherData = new DailyWeatherData(nearDate,
                dailyTemperature,
                null,
                null,
                null);
        Temperature nearTemperature = new Temperature(
                new WaterLayerState(24, HeatLevel.COMFORT, 0, WaterTrend.STABLE),
                new WaterLayerState(19, HeatLevel.COMFORT, 0, WaterTrend.STABLE)
        );
        TemperatureState expected = new TemperatureState(
                0,
                1,
                null,
                nearTemperature);
        TemperatureState actual = analyser.analyze(List.of(weatherData), localDate);
        assertEquals(expected, actual);
    }

    @Test
    void shouldUseNightTemperatureWhenDayTemperatureIsMissing() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate nearDate = localDate.minusDays(1);
        DailyTemperature dailyTemperature = new DailyTemperature(nearDate,
                null,
                25.0);
        DailyWeatherData weatherData = new DailyWeatherData(nearDate,
                dailyTemperature,
                null,
                null,
                null);
        Temperature nearTemperature = new Temperature(
                new WaterLayerState(24, HeatLevel.COMFORT, 0, WaterTrend.STABLE),
                new WaterLayerState(19, HeatLevel.COMFORT, 0, WaterTrend.STABLE)
        );
        TemperatureState expected = new TemperatureState(
                0,
                1,
                null,
                nearTemperature);
        TemperatureState actual = analyser.analyze(List.of(weatherData), localDate);
        assertEquals(expected, actual);
    }

    @Test
    void shouldIgnoreTemperatureWhenDayAndNightAreMissing() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate nearDate = localDate.minusDays(1);
        DailyTemperature dailyTemperature = new DailyTemperature(nearDate,
                null,
                null);
        DailyWeatherData weatherData = new DailyWeatherData(nearDate,
                dailyTemperature,
                null,
                null,
                null);

        TemperatureState expected = new TemperatureState(
                0,
                0,
                null,
                null);
        TemperatureState actual = analyser.analyze(List.of(weatherData), localDate);
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCoolAtTemperature14() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate nearDate = localDate.minusDays(1);
        DailyTemperature dailyTemperature = new DailyTemperature(nearDate,
                14.0,
                14.0);
        DailyWeatherData weatherData = new DailyWeatherData(nearDate,
                dailyTemperature,
                null,
                null,
                null);
        TemperatureState actual = analyser.analyze(List.of(weatherData), localDate);
        assertEquals(HeatLevel.COOL, actual.getTemperatureNearPeriod().getUpperLayerState().getHeatLevel());
    }

    @Test
    void shouldReturnComfortAtTemperature17() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate nearDate = localDate.minusDays(1);
        DailyTemperature dailyTemperature = new DailyTemperature(nearDate,
                17.7,
                17.7);
        DailyWeatherData weatherData = new DailyWeatherData(nearDate,
                dailyTemperature,
                null,
                null,
                null);
        TemperatureState actual = analyser.analyze(List.of(weatherData), localDate);
        assertEquals(HeatLevel.COMFORT, actual.getTemperatureNearPeriod().getUpperLayerState().getHeatLevel());
    }

    @Test
    void shouldReturnWarmAtTemperature27() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate nearDate = localDate.minusDays(1);
        DailyTemperature dailyTemperature = new DailyTemperature(nearDate,
                28.4,
                28.4);
        DailyWeatherData weatherData = new DailyWeatherData(nearDate,
                dailyTemperature,
                null,
                null,
                null);
        TemperatureState actual = analyser.analyze(List.of(weatherData), localDate);
        assertEquals(HeatLevel.WARM, actual.getTemperatureNearPeriod().getUpperLayerState().getHeatLevel());
    }

    @Test
    void shouldReturnHotAtTemperature30() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate nearDate = localDate.minusDays(1);
        DailyTemperature dailyTemperature = new DailyTemperature(nearDate,
                31.55,
                31.55);
        DailyWeatherData weatherData = new DailyWeatherData(nearDate,
                dailyTemperature,
                null,
                null,
                null);
        TemperatureState actual = analyser.analyze(List.of(weatherData), localDate);
        assertEquals(HeatLevel.HOT, actual.getTemperatureNearPeriod().getUpperLayerState().getHeatLevel());
    }

    @Test
    void shouldReturnFallingTrendAtMinusTwo() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);

        DailyWeatherData day1 = new DailyWeatherData(
                LocalDate.of(2026, 8, 21),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 21),
                        20.0,
                        20.0),
                null, null, null);

        DailyWeatherData day2 = new DailyWeatherData(
                LocalDate.of(2026, 8, 22),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 22),
                        13.0,
                        13.0),
                null, null, null);

        TemperatureState actual = analyser.analyze(
                List.of(day1, day2),
                localDate);

        assertEquals(
                WaterTrend.FALLING,
                actual.getTemperatureFarPeriod()
                        .getUpperLayerState()
                        .getTemperatureTrend());
    }

    @Test
    void shouldReturnStableTrendAtMinusOne() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);

        DailyWeatherData day1 = new DailyWeatherData(
                LocalDate.of(2026, 8, 21),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 21),
                        20.0,
                        20.0),
                null, null, null);

        DailyWeatherData day2 = new DailyWeatherData(
                LocalDate.of(2026, 8, 22),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 22),
                        14.5,
                        14.5),
                null, null, null);

        TemperatureState actual = analyser.analyze(
                List.of(day1, day2),
                localDate);

        assertEquals(
                WaterTrend.STABLE,
                actual.getTemperatureFarPeriod()
                        .getUpperLayerState()
                        .getTemperatureTrend());
    }

    @Test
    void shouldReturnStableTrendAtOne() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);

        DailyWeatherData day1 = new DailyWeatherData(
                LocalDate.of(2026, 8, 21),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 21),
                        20.0,
                        20.0),
                null, null, null);

        DailyWeatherData day2 = new DailyWeatherData(
                LocalDate.of(2026, 8, 22),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 22),
                        22.0,
                        22.0),
                null, null, null);

        TemperatureState actual = analyser.analyze(
                List.of(day1, day2),
                localDate);

        assertEquals(
                WaterTrend.STABLE,
                actual.getTemperatureFarPeriod()
                        .getUpperLayerState()
                        .getTemperatureTrend());
    }

    @Test
    void shouldReturnRisingTrendAtTwo() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);

        DailyWeatherData day1 = new DailyWeatherData(
                LocalDate.of(2026, 8, 21),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 21),
                        20.0,
                        20.0),
                null, null, null);

        DailyWeatherData day2 = new DailyWeatherData(
                LocalDate.of(2026, 8, 22),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 22),
                        26.0,
                        26.0),
                null, null, null);

        TemperatureState actual = analyser.analyze(
                List.of(day1, day2), localDate);

        assertEquals(
                WaterTrend.RISING,
                actual.getTemperatureFarPeriod()
                        .getUpperLayerState()
                        .getTemperatureTrend());
    }


    @Test
    void shouldCalculateMiddleLayerTemperatureFiveDegreesLower() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        LocalDate nearDate = localDate.minusDays(1);

        DailyTemperature dailyTemperature = new DailyTemperature(
                nearDate,
                18.0,
                18.0);

        DailyWeatherData weatherData = new DailyWeatherData(
                nearDate,
                dailyTemperature,
                null,
                null,
                null);

        TemperatureState actual = analyser.analyze(
                List.of(weatherData),
                localDate);

        assertEquals(
                5,
                actual.getTemperatureNearPeriod()
                        .getUpperLayerState()
                        .getHeadTemp()
                        - actual.getTemperatureNearPeriod()
                        .getMiddleLayerState()
                        .getHeadTemp());
    }

    @Test
    void shouldCalculateMiddleLayerTrendAsHalfOfUpperLayerTrend() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);

        DailyWeatherData day1 = new DailyWeatherData(
                LocalDate.of(2026, 8, 21),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 21),
                        20.0,
                        20.0),
                null, null, null);

        DailyWeatherData day2 = new DailyWeatherData(
                LocalDate.of(2026, 8, 22),
                new DailyTemperature(
                        LocalDate.of(2026, 8, 22),
                        26.0,
                        26.0),
                null, null, null);

        TemperatureState actual = analyser.analyze(
                List.of(day1, day2),
                localDate);

        assertEquals(
                2,
                actual.getTemperatureFarPeriod()
                        .getUpperLayerState()
                        .getHeadTrend());

        assertEquals(
                1,
                actual.getTemperatureFarPeriod()
                        .getMiddleLayerState()
                        .getHeadTrend());
    }

}
