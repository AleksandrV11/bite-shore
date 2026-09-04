package com.fishing.bite_shore.service.analyzer;

import com.fishing.bite_shore.entity.WeatherDataEntity;
import com.fishing.bite_shore.exception.InvalidPressureException;
import com.fishing.bite_shore.model.analysis.precipitation.PrecipitationLevel;
import com.fishing.bite_shore.model.analysis.pressure.PressureLevel;
import com.fishing.bite_shore.model.analysis.pressure.PressureMovement;
import com.fishing.bite_shore.model.analysis.pressure.PressureState;
import com.fishing.bite_shore.model.analysis.pressure.PressureTrend;
import com.fishing.bite_shore.model.daily.DailyPressure;
import com.fishing.bite_shore.model.daily.DailyWeatherData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PressureAnalyserTest {
    PressureAnalyzer analyser = new PressureAnalyzer();

    @Test
    void shouldThrowExceptionWhenPressureDataIsEmpty() {
        List<DailyWeatherData> dailyWeatherData = List.of();
        InvalidPressureException exception = assertThrows(InvalidPressureException.class,
                () -> analyser.analyze(dailyWeatherData,
                        750.0,
                        LocalDate.of(2026, 8, 24)));
        assertEquals("Немає даних тиску", exception.getMessage());
    }
//if (lastPressure.isEmpty()) {
//        level = NOT_DATA;
//        movement = NOT_DATA;
//        pressureTrend = NOT_DATA;
//    } else {
//    ...

    @Test
    void shouldReturnNotDataWhenPreviousDayPressureIsMissing() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        LocalDate dayMinus2 = localDate.minusDays(2);
        DailyPressure dailyPressure = new DailyPressure(dayMinus2
                , 750
                , 750);

        DailyWeatherData dailyWeatherData = new DailyWeatherData(dayMinus2,
                null,
                dailyPressure,
                null,
                null);
        PressureState pressure = analyser.analyze(
                List.of(dailyWeatherData),
                750.0,
                localDate
        );
        assertEquals(PressureLevel.NOT_DATA, pressure.getPressureLevel());
        assertEquals(PressureMovement.NOT_DATA, pressure.getPressureMovement());
        assertEquals(PressureTrend.NOT_DATA, pressure.getPressureTrend());
    }

    @Test
    void shouldReturnLowPressureBelowBoundary() {
        LocalDate localDate = LocalDate.of(2026, 8, 25);

        DailyPressure pressureMinus1 = new DailyPressure(localDate.minusDays(1),
                750,
                750);
        DailyPressure pressureMinus2 = new DailyPressure(localDate.minusDays(2),
                750,
                750);
        DailyWeatherData dailyWeatherDataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureMinus1,
                null,
                null);
        DailyWeatherData dailyWeatherDataDay2 = new DailyWeatherData(localDate.minusDays(2),
                null,
                pressureMinus2,
                null,
                null);
        PressureState state = analyser.analyze(List.of(dailyWeatherDataDay1, dailyWeatherDataDay2),
                0.0,
                localDate);
        assertEquals(PressureLevel.LOW, state.getPressureLevel());
    }

    @Test
    void shouldReturnNormalPressureAtLowerBoundary() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                757,
                757);
        DailyPressure pressureDay2 = new DailyPressure(localDate.minusDays(2),
                757,
                757);
        DailyWeatherData weatherDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);
        DailyWeatherData weatherDay2 = new DailyWeatherData(localDate.minusDays(2),
                null,
                pressureDay2,
                null,
                null);
        PressureState state = analyser.analyze(List.of(weatherDay1, weatherDay2), 0.0, localDate);
        assertEquals(PressureLevel.NORMAL, state.getPressureLevel());
    }

    @Test
    void shouldReturnNormalPressureAtUpperBoundary() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                763,
                763);
        DailyPressure pressureDay2 = new DailyPressure(localDate.minusDays(2),
                763,
                763);
        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);
        DailyWeatherData dataDay2 = new DailyWeatherData(localDate.minusDays(2),
                null,
                pressureDay2,
                null,
                null);
        PressureState state = analyser.analyze(List.of(dataDay1, dataDay2),
                0.0,
                localDate);
        assertEquals(PressureLevel.NORMAL, state.getPressureLevel());
    }

    @Test
    void shouldReturnHighPressureAboveBoundary() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                764,
                764);
        DailyPressure pressureDay2 = new DailyPressure(localDate.minusDays(2),
                764,
                763);
        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);
        DailyWeatherData dataDay2 = new DailyWeatherData(localDate.minusDays(2),
                null,
                pressureDay2,
                null,
                null);
        PressureState state = analyser.analyze(List.of(dataDay1, dataDay2),
                0.0,
                localDate);
        assertEquals(PressureLevel.HIGH, state.getPressureLevel());
    }

    @Test
    void shouldReturnFallingMovementWhenPressureDecreasesMoreThanThree() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                764,
                760);

        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);

        PressureState state = analyser.analyze(List.of(dataDay1),
                0.0,
                localDate);
        assertEquals(PressureTrend.FALLING, state.getPressureTrend());
    }

    @Test
    void shouldReturnStableMovementWhenPressureChangesByThree() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                765,
                762);
        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);
        PressureState state = analyser.analyze(List.of(dataDay1),
                0.0,
                localDate);
        assertEquals(PressureMovement.STABLE, state.getPressureMovement());
    }

    @Test
    void shouldReturnStableMovementWhenPressureDoesNotChange() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                765,
                765);
        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);
        PressureState state = analyser.analyze(List.of(dataDay1),
                0.0,
                localDate);
        assertEquals(PressureMovement.STABLE, state.getPressureMovement());
    }

    @Test
    void shouldReturnRisingMovementWhenPressureIncreasesMoreThanThree() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                764,
                764);
        DailyPressure pressureDay2 = new DailyPressure(localDate.minusDays(2),
                760,
                760);
        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);
        DailyWeatherData dataDay2 = new DailyWeatherData(localDate.minusDays(2),
                null,
                pressureDay2,
                null,
                null);
        PressureState state = analyser.analyze(List.of(dataDay2, dataDay1),
                0.0,
                localDate);
        assertEquals(PressureMovement.RISING, state.getPressureMovement());
    }

    @Test
    void shouldReturnFallingTrendWhenNightPressureIsLowerThanDayPressure() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                764,
                760);

        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);

        PressureState state = analyser.analyze(List.of( dataDay1),
                0.0,
                localDate);
        assertEquals(PressureTrend.FALLING, state.getPressureTrend());
    }

    @Test
    void shouldReturnRisingTrendWhenNightPressureIsHigherThanDayPressure() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                760,
                764);

        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);

        PressureState state = analyser.analyze(List.of( dataDay1),
                0.0,
                localDate);
        assertEquals(PressureTrend.RISING, state.getPressureTrend());
    }

    @Test
    void shouldReturnStabilizedLowTrend() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                756,
                756);

        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);

        PressureState state = analyser.analyze(List.of( dataDay1),
                0.0,
                localDate);
        assertEquals(PressureTrend.STABILIZED_LOW, state.getPressureTrend());
    }

    @Test
    void shouldReturnStabilizedNormalTrend() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                758,
                758);

        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);

        PressureState state = analyser.analyze(List.of( dataDay1),
                0.0,
                localDate);
        assertEquals(PressureTrend.STABILIZED_NORMAL, state.getPressureTrend());
    }

    @Test
    void shouldReturnStabilizedHighTrend() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                764,
                764);

        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);

        PressureState state = analyser.analyze(List.of( dataDay1),
                0.0,
                localDate);
        assertEquals(PressureTrend.STABILIZED_HIGH, state.getPressureTrend());
    }

    @Test
    void shouldReturnNotDataTrendWhenDayOrNightPressureIsMissing() {
        LocalDate localDate = LocalDate.of(2026, 8, 24);
        DailyPressure pressureDay1 = new DailyPressure(localDate.minusDays(1),
                764,
                null);

        DailyWeatherData dataDay1 = new DailyWeatherData(localDate.minusDays(1),
                null,
                pressureDay1,
                null,
                null);

        PressureState state = analyser.analyze(List.of( dataDay1),
                0.0,
                localDate);
        assertEquals(PressureTrend.NOT_DATA, state.getPressureTrend());
    }

}
