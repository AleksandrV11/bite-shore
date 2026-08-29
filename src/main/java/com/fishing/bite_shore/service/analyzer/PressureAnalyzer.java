package com.fishing.bite_shore.service.analyzer;

import com.fishing.bite_shore.entity.WeatherLocationEntity;
import com.fishing.bite_shore.exception.InvalidPressureException;
import com.fishing.bite_shore.model.analysis.pressure.PressureTrend;
import com.fishing.bite_shore.model.analysis.pressure.PressureLevel;
import com.fishing.bite_shore.model.analysis.pressure.PressureMovement;
import com.fishing.bite_shore.model.analysis.pressure.PressureState;
import com.fishing.bite_shore.model.daily.DailyPressure;
import com.fishing.bite_shore.model.daily.DailyWeatherData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

@Service
public class PressureAnalyzer {
    public PressureState analyze(List<DailyWeatherData> dailyWeatherData, Double elevation, LocalDate localDate) {

        List<Integer> pressureList = dailyWeatherData.stream()
                .map(DailyWeatherData::getDailyPressure)
                .flatMap(a -> Stream.of(a.getAveragePressureDay(), a.getAveragePressureNight()))
                .filter(Objects::nonNull)
                .map(p -> convertSeaLevelPressureToLocal(p, elevation))
                .toList();
        if (pressureList.isEmpty()) {
            throw new InvalidPressureException("Немає даних тиску");
        }
        OptionalDouble lastPressure = dailyWeatherData.stream()
                .filter(data -> data.getLocalDate().equals(localDate.minusDays(1)))
                .map(DailyWeatherData::getDailyPressure)
                .flatMap(p -> Stream.of(
                        p.getAveragePressureDay(),
                        p.getAveragePressureNight()
                ))
                .filter(Objects::nonNull)
                .mapToInt(p -> convertSeaLevelPressureToLocal(p, elevation))
                .average();

        int normalPressure = calculateNormalLocalPressure(elevation);
        int startaPressure = pressureList.get(0);
        int lastAveragePressure = 0;
        PressureLevel level;
        PressureMovement movement;
        PressureTrend pressureTrend;

        if (lastPressure.isEmpty()) {
            level = PressureLevel.NOT_DATA;
            movement = PressureMovement.NOT_DATA;
            pressureTrend = PressureTrend.NOT_DATA;
        } else {
            lastAveragePressure = (int) Math.round(lastPressure.getAsDouble());

            int trendPressure = lastAveragePressure - startaPressure;
            if (startaPressure > normalPressure + 3) {
                level = PressureLevel.HIGH;
            } else if (startaPressure < normalPressure - 3) {
                level = PressureLevel.LOW;
            } else {
                level = PressureLevel.NORMAL;
            }
            if (trendPressure < -3) {
                movement = PressureMovement.FALLING;
            } else if (trendPressure > 3) {
                movement = PressureMovement.RISING;
            } else {
                movement = PressureMovement.STABLE;
            }
            int lastDay = dailyWeatherData.stream()
                    .filter(data -> data.getLocalDate().equals(localDate.minusDays(1)))
                    .map(DailyWeatherData::getDailyPressure)
                    .map(DailyPressure::getAveragePressureDay)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(0);

            int lastNight = dailyWeatherData.stream()
                    .filter(data -> data.getLocalDate().equals(localDate.minusDays(1)))
                    .map(DailyWeatherData::getDailyPressure)
                    .map(DailyPressure::getAveragePressureNight)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(0);

            int trendRecentPressure = 0;
            if (lastDay == 0 || lastNight == 0) {
                pressureTrend = PressureTrend.NOT_DATA;
            } else {
                trendRecentPressure = lastNight - lastDay;

                if (trendRecentPressure <= -2) {
                    pressureTrend = PressureTrend.FALLING;
                } else if (trendRecentPressure >= 2) {
                    pressureTrend = PressureTrend.RISING;
                } else {
                    if (level == PressureLevel.HIGH) {
                        pressureTrend = PressureTrend.STABILIZED_HIGH;
                    } else if (level == PressureLevel.LOW) {
                        pressureTrend = PressureTrend.STABILIZED_LOW;
                    } else {
                        pressureTrend = PressureTrend.STABILIZED_NORMAL;
                    }
                }
            }
        }
        return new PressureState(startaPressure, lastAveragePressure, normalPressure, level, movement,
                pressureTrend);
    }

    private int convertSeaLevelPressureToLocal(int seaLevelPressure, double elevation) {
        return (int) Math.round(seaLevelPressure *
                Math.pow(1 - 2.25577e-5 * elevation, 5.25588));
    }

    private int calculateNormalLocalPressure(double elevation) {
        return (int) Math.round(
                760 * Math.pow(1 - 2.25577e-5 * elevation, 5.25588)
        );
    }

}
