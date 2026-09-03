package com.fishing.bite_shore.service.analyzer;

import com.fishing.bite_shore.exception.InvalidPrecipitationException;
import com.fishing.bite_shore.model.analysis.precipitation.Precipitation;
import com.fishing.bite_shore.model.analysis.precipitation.PrecipitationLevel;
import com.fishing.bite_shore.model.analysis.precipitation.PrecipitationState;
import com.fishing.bite_shore.model.daily.DailyPrecipitation;
import com.fishing.bite_shore.model.daily.DailyWeatherData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrecipitationAnalyserTest {

    private final PrecipitationAnalyser analyser = new PrecipitationAnalyser();

    @Test
    void shouldThrowExceptionWhenPrecipitationDataIsEmpty() {
        List<DailyWeatherData> weatherDataList = List.of();
        InvalidPrecipitationException exception = assertThrows(InvalidPrecipitationException.class,
                () -> analyser.analyze(weatherDataList,
                        LocalDate.of(2026, 1, 02)));
        assertEquals("Даних по дощу немає", exception.getMessage());
    }

    @Test
    void shouldReturnNoneWhenPrecipitationIsZero() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        DailyPrecipitation dailyPrecipitation = new DailyPrecipitation(localDate,
                0, 0, 50, 50);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(localDate.minusDays(1),
                null,
                null,
                null,
                dailyPrecipitation);
        PrecipitationState state = analyser.analyze(
                List.of(dailyWeatherData),
                localDate
        );
        assertEquals(PrecipitationLevel.NONE, state.getDayMinus1().getLevel());
    }

    @Test
    void shouldReturnLightWhenPrecipitationIsLessThanFive() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        DailyPrecipitation dailyPrecipitation = new DailyPrecipitation(localDate,
                3, 4, 50, 50);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(localDate.minusDays(1),
                null,
                null,
                null,
                dailyPrecipitation);
        PrecipitationState state = analyser.analyze(
                List.of(dailyWeatherData),
                localDate
        );
        assertEquals(PrecipitationLevel.LIGHT, state.getDayMinus1().getLevel());
    }

    @Test
    void shouldReturnModerateWhenPrecipitationIsFromFiveToFourteen() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        DailyPrecipitation dailyPrecipitation = new DailyPrecipitation(localDate,
                7, 8, 50, 50);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(localDate.minusDays(1),
                null,
                null,
                null,
                dailyPrecipitation);
        PrecipitationState state = analyser.analyze(
                List.of(dailyWeatherData),
                localDate
        );
        assertEquals(PrecipitationLevel.MODERATE, state.getDayMinus1().getLevel());
    }

    @Test
    void shouldReturnHeavyWhenPrecipitationIsFifteenOrMore() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        DailyPrecipitation dailyPrecipitation = new DailyPrecipitation(localDate,
                16, 15, 50, 50);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(localDate.minusDays(1),
                null,
                null,
                null,
                dailyPrecipitation);
        PrecipitationState state = analyser.analyze(
                List.of(dailyWeatherData),
                localDate
        );
        assertEquals(PrecipitationLevel.HEAVY, state.getDayMinus1().getLevel());
    }

    @Test
    void shouldReturnPrecipitationForDayMinusOne() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        LocalDate dayMinus1 = localDate.minusDays(1);
        DailyPrecipitation dailyPrecipitation = new DailyPrecipitation(dayMinus1,
                6, 15, 50, 60);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(dayMinus1,
                null,
                null,
                null,
                dailyPrecipitation);
        PrecipitationState precipitation = analyser.analyze(
                List.of(dailyWeatherData),
                localDate
        );
        assertEquals(PrecipitationLevel.HEAVY, precipitation.getDayMinus1().getLevel());
        assertEquals(55, precipitation.getDayMinus1().getPopPercent());
        assertEquals(15, precipitation.getDayMinus1().getPrecipitation());
    }

    @Test
    void shouldReturnPrecipitationForDayMinusTwo() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        LocalDate dayMinus2 = localDate.minusDays(2);
        DailyPrecipitation dailyPrecipitation = new DailyPrecipitation(dayMinus2,
                6, 17, 70, 60);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(dayMinus2,
                null,
                null,
                null,
                dailyPrecipitation);
        PrecipitationState precipitation = analyser.analyze(
                List.of(dailyWeatherData),
                localDate
        );
        assertEquals(PrecipitationLevel.HEAVY, precipitation.getDayMinus2().getLevel());
        assertEquals(65, precipitation.getDayMinus2().getPopPercent());
        assertEquals(17, precipitation.getDayMinus2().getPrecipitation());
    }

    @Test
    void shouldReturnNullWhenDayMinusOneDataIsMissing() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        LocalDate dayMinus2 = localDate.minusDays(2);
        DailyPrecipitation dailyPrecipitation = new DailyPrecipitation(dayMinus2,
                4,
                4,
                50,
                60);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(dayMinus2,
                null,
                null,
                null,
                dailyPrecipitation);
        PrecipitationState precipitation = analyser.analyze(List.of(dailyWeatherData), localDate);
        assertNull(precipitation.getDayMinus1());

    }

    @Test
    void shouldReturnNullWhenDayMinusTwoDataIsMissing() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        LocalDate dayMinus1 = localDate.minusDays(1);
        DailyPrecipitation dailyPrecipitation = new DailyPrecipitation(dayMinus1,
                3,
                3,
                4,
                4);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(dayMinus1,
                null,
                null,
                null,
                dailyPrecipitation);
        PrecipitationState precipitation = analyser.analyze(List.of(dailyWeatherData), localDate);
        assertNull(precipitation.getDayMinus2());

    }

    @Test
    void shouldCalculateAveragePopPercent() {
        LocalDate localDate = LocalDate.now().plusDays(3);
        LocalDate dayMinus2 = localDate.minusDays(2);
        DailyPrecipitation dailyPrecipitation = new DailyPrecipitation(dayMinus2,
                6, 17, 70, 60);
        DailyWeatherData dailyWeatherData = new DailyWeatherData(dayMinus2,
                null,
                null,
                null,
                dailyPrecipitation);
        PrecipitationState precipitation = analyser.analyze(
                List.of(dailyWeatherData),
                localDate
        );

        assertEquals(65, precipitation.getDayMinus2().getPopPercent());
    }
}