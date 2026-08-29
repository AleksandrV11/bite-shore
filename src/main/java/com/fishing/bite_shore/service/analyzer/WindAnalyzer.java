package com.fishing.bite_shore.service.analyzer;

import com.fishing.bite_shore.exception.InvalidWindException;
import com.fishing.bite_shore.model.analysis.wind.Wind;
import com.fishing.bite_shore.model.analysis.wind.WindDirection;
import com.fishing.bite_shore.model.analysis.wind.WindState;
import com.fishing.bite_shore.model.analysis.wind.WindStrength;
import com.fishing.bite_shore.model.daily.DailyWeatherData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class WindAnalyzer {

    public WindState analyze(List<DailyWeatherData> dailyWeatherData, LocalDate localDate) {


        if (dailyWeatherData.isEmpty()) {
            throw new InvalidWindException("Немає даних по вітру");
        }

        List<DailyWeatherData> farPeriod = dailyWeatherData.stream()
                .filter(data -> !data.getLocalDate().isBefore(localDate.minusDays(4)))
                .filter(data -> data.getLocalDate().isBefore(localDate.minusDays(1)))
                .toList();
        List<DailyWeatherData> nearPeriod = dailyWeatherData.stream()
                .filter(data -> data.getLocalDate().equals(localDate.minusDays(1)))
                .toList();

        double averageEffictiveWindFarDay = farPeriod.stream()
                .map(DailyWeatherData::getDailyWind)
                .mapToDouble(a -> (a.getEffectiveWindSpeedDay() + a.getEffectiveWindSpeedNight()) / 2)
                .average().orElse(0);

        double averageEffictiveWindNearDay = nearPeriod.stream()
                .filter(data -> data.getLocalDate().equals(localDate.minusDays(1)))
                .map(DailyWeatherData::getDailyWind)
                .mapToDouble(a -> (a.getEffectiveWindSpeedDay() + a.getEffectiveWindSpeedNight()) / 2)
                .average().orElse(0);

        List<Double> directoryWindFarDay = farPeriod.stream()
                .map(DailyWeatherData::getDailyWind)
                .map(a -> calculateAverageDirection(
                        a.getAverageWindDirectionDay(),
                        a.getAverageWindDirectionNight()))
                .filter(Objects::nonNull)
                .mapToDouble(Integer::doubleValue)
                .boxed()
                .toList();
        List<Double> directoryWindNearDay = nearPeriod.stream()
                .map(DailyWeatherData::getDailyWind)
                .map(a -> calculateAverageDirection(
                        a.getAverageWindDirectionDay(),
                        a.getAverageWindDirectionNight()))
                .filter(Objects::nonNull)
                .mapToDouble(Integer::doubleValue)
                .boxed()
                .toList();

        Wind farWind;

        if (!farPeriod.isEmpty()) {
          Integer averageDirectoryFar = calculateAverageDirection(directoryWindFarDay);
            farWind =
                    new Wind((int) averageEffictiveWindFarDay, determineWindStrength(averageEffictiveWindFarDay),
                            determineWindDirection(averageDirectoryFar));
        } else {
            farWind = new Wind(0, WindStrength.NO_DATA_STRENGHT, WindDirection.NO_DATA_DIRECTION);
        }
        Wind nearWind;
        if (!nearPeriod.isEmpty()) {
            Integer averageDirectoryNear = calculateAverageDirection(directoryWindNearDay);
            nearWind = new Wind((int) averageEffictiveWindNearDay, determineWindStrength(averageEffictiveWindNearDay)
                    , determineWindDirection(averageDirectoryNear));
        } else {
            nearWind = new Wind(0, WindStrength.NO_DATA_STRENGHT, WindDirection.NO_DATA_DIRECTION);
        }

        return new WindState(0, 0, farWind, nearWind);
    }

    private WindStrength determineWindStrength(double windSpeed) {
        if (windSpeed == 0) {
            return WindStrength.NO_DATA_STRENGHT;
        } else if (windSpeed < 2) {
            return WindStrength.CALM;
        } else if (windSpeed < 4) {
            return WindStrength.LIGHT;
        } else if (windSpeed < 6) {
            return WindStrength.MODERATE;
        } else if (windSpeed < 8) {
            return WindStrength.STRONG;
        } else {
            return WindStrength.VERY_STRONG;
        }
    }

    private WindDirection determineWindDirection(Integer windDirection) {

        if (windDirection == null) {
            return WindDirection.NO_DATA_DIRECTION;
        }

        if (windDirection >= 337.5 || windDirection < 22.5) {
            return WindDirection.N;
        } else if (windDirection < 67.5) {
            return WindDirection.NE;
        } else if (windDirection < 112.5) {
            return WindDirection.E;
        } else if (windDirection < 157.5) {
            return WindDirection.SE;
        } else if (windDirection < 202.5) {
            return WindDirection.S;
        } else if (windDirection < 247.5) {
            return WindDirection.SW;
        } else if (windDirection < 292.5) {
            return WindDirection.W;
        } else {
            return WindDirection.NW;
        }
    }

    private Integer calculateAverageDirection(Integer directoryDay, Integer directoryNight) {

        if (directoryDay == null && directoryNight == null) {
            return null;
        }
        if (directoryDay == null) {
            return directoryNight;
        }
        if (directoryNight == null) {
            return directoryDay;
        }

        double sin = Math.sin(Math.toRadians(directoryDay)) + Math.sin(Math.toRadians(directoryNight));
        double cos = Math.cos(Math.toRadians(directoryDay)) + Math.cos(Math.toRadians(directoryNight));
        double angle = Math.toDegrees(Math.atan2(sin, cos));

        if (angle < 0) {
            angle += 360;
        }
        return (int) Math.round(angle);
    }

    private Integer calculateAverageDirection(List<Double> directions) {
        if (directions.isEmpty()) {
            return null;
        }
        double sin = directions.stream().mapToDouble(direction -> Math.sin(Math.toRadians(direction))).sum();
        double cos = directions.stream().mapToDouble(direction -> Math.cos(Math.toRadians(direction))).sum();
        double angle = Math.toDegrees(Math.atan2(sin, cos));

        if (angle < 0) {
            angle += 360;
        }

        return (int) Math.round(angle);
    }


}
