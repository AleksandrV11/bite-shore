package com.fishing.bite_shore.service.analyzer;

import com.fishing.bite_shore.exception.InvalidTemperatureException;
import com.fishing.bite_shore.model.analysis.temperature.*;
import com.fishing.bite_shore.model.daily.DailyTemperature;
import com.fishing.bite_shore.model.daily.DailyWeatherData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class TemperatureAnalyzer {
    public TemperatureState analyze(
            List<DailyWeatherData> dailyWeatherData,
            LocalDate localDate) {

        List<Double> averageAirTemperatureFarPeriod = getAverageAirTemperatureFarPeriod(dailyWeatherData, localDate);
        List<Double> averageAirTemperatureNearPeriod = getAverageAirTemperatureNearPeriod(dailyWeatherData, localDate);

        if (averageAirTemperatureFarPeriod.isEmpty() && averageAirTemperatureNearPeriod.isEmpty()) {

            return new TemperatureState(
                    0,
                    0,
                    null,
                    null);
        }

        List<Double> waterTemperatureFarPeriod = new ArrayList<>();
        List<Double> waterTemperatureNearPeriod = new ArrayList<>();

        if (!averageAirTemperatureFarPeriod.isEmpty()) {
            double previousWaterTemp = averageAirTemperatureFarPeriod.get(0) * 0.95;
            waterTemperatureFarPeriod = getWaterTemperature(averageAirTemperatureFarPeriod, previousWaterTemp);
        }

        if (!averageAirTemperatureNearPeriod.isEmpty()) {
            double previousWaterTemp;

            if (!waterTemperatureFarPeriod.isEmpty()) {
                previousWaterTemp = waterTemperatureFarPeriod.get(waterTemperatureFarPeriod.size() - 1);

            } else {
                previousWaterTemp = averageAirTemperatureNearPeriod.get(0) * 0.95;
            }

            waterTemperatureNearPeriod = getWaterTemperature(averageAirTemperatureNearPeriod, previousWaterTemp);
        }

        Temperature temperatureFarPeriod = analyzeWaterTemperature(waterTemperatureFarPeriod);
        Temperature temperatureNearPeriod = analyzeWaterTemperature(waterTemperatureNearPeriod);

        return new TemperatureState(
                waterTemperatureFarPeriod.size(),
                waterTemperatureNearPeriod.size(),
                temperatureFarPeriod,
                temperatureNearPeriod);
    }

    private Temperature analyzeWaterTemperature(List<Double> waterTemperature) {
        if (waterTemperature.isEmpty()) {
            return null;
        }

        int temperatureTrend = (int) Math.round(
                waterTemperature.get(waterTemperature.size() - 1)
                        - waterTemperature.get(0));

        int averageTemperature = (int) Math.round(
                waterTemperature.stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0));

        HeatLevel upperHeatLevel = determineHeatLevel(averageTemperature);
        WaterTrend upperTrend = determineTrend(temperatureTrend, 2);

        WaterLayerState upperLayerState =
                new WaterLayerState(
                        averageTemperature,
                        upperHeatLevel,
                        temperatureTrend,
                        upperTrend
                );

        int middleTemperature = averageTemperature - 5;
        HeatLevel middleHeatLevel = determineHeatLevel(middleTemperature);
        int middleTemperatureTrend = (int) Math.round(temperatureTrend * 0.5);
        WaterTrend middleTrend = determineTrend(middleTemperatureTrend, 2);

        WaterLayerState middleLayerState =
                new WaterLayerState(
                        middleTemperature,
                        middleHeatLevel,
                        middleTemperatureTrend,
                        middleTrend
                );

        return new Temperature(upperLayerState, middleLayerState);
    }

    private List<Double> getAverageAirTemperatureFarPeriod(List<DailyWeatherData> dailyWeatherData, LocalDate localdata) {
        return dailyWeatherData.stream()
                .filter(data -> data.getLocalDate().isBefore(localdata.minusDays(1)))
                .map(DailyWeatherData::getDailyTemperature)
                .map(this::calculateAverageAirTemperature)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Double> getAverageAirTemperatureNearPeriod(List<DailyWeatherData> dailyWeatherData,
                                                            LocalDate localData) {
        return dailyWeatherData.stream()
                .filter(data -> data.getLocalDate().equals(localData.minusDays(1)))
                .map(DailyWeatherData::getDailyTemperature)
                .map(this::calculateAverageAirTemperature)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Double> getAverageAirTemperature(List<DailyWeatherData> dailyWeatherData) {
        return dailyWeatherData.stream()
                .map(DailyWeatherData::getDailyTemperature)
                .map(this::calculateAverageAirTemperature)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Double> getWaterTemperature(List<Double> averageAirTemperature, Double previousWaterTemp) {
        double previousTemp = previousWaterTemp;
        List<Double> temperatureWater = new ArrayList<>();
        for (Double tempAir : averageAirTemperature) {
            double waterTemperature = previousTemp * 0.7 + tempAir * 0.3;
            temperatureWater.add(waterTemperature);
            previousTemp = waterTemperature;
        }

        return temperatureWater;
    }

    private Double calculateAverageAirTemperature(DailyTemperature temperature) {

        Double day = temperature.getAverageTemperatureDay();
        Double night = temperature.getAverageTemperatureNight();

        if (day == null && night == null) {
            return null;
        }
        if (day == null) {
            return night;
        }
        if (night == null) {
            return day;
        }

        return (day + night) / 2;
    }

    private HeatLevel determineHeatLevel(int temperatureWater) {
        if (temperatureWater < 14) {
            return HeatLevel.COLD;         // Вода холодна.  Обмін речовин у риби сповільнений.
        } else if (temperatureWater < 17) {
            return HeatLevel.COOL;         // Вода прохолодна. Активність є, але нижча від оптимальної.
        } else if (temperatureWater < 27) {
            return HeatLevel.COMFORT;      // Комфортний температурний режим. Найбільш сприятлива зона для
            // активного харчування.
        } else if (temperatureWater < 30) {
            return HeatLevel.WARM;         // Тепла вода.   Активний обмін речовин, але треба враховувати кисень.
        } else {
            return HeatLevel.HOT;          // Перегріта вода. Температура вже може негативно впливати.
        }
    }

    private WaterTrend determineTrend(int temperatureTrend, int threshold) {
        if (temperatureTrend <= -threshold) {
            return WaterTrend.FALLING;
        } else if (temperatureTrend >= threshold) {
            return WaterTrend.RISING;
        } else {
            return WaterTrend.STABLE;
        }
    }
}
