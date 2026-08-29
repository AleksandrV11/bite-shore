package com.fishing.bite_shore.service.aggregator;

import com.fishing.bite_shore.exception.WeatherWindDirectionException;
import com.fishing.bite_shore.extractor.WindExtractor;
import com.fishing.bite_shore.model.WeatherSnapshot;
import com.fishing.bite_shore.model.daily.DailyWind;
import com.fishing.bite_shore.model.point.WindPoint;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WindAggregator {
    private final WindExtractor windExtractor;

    public List<DailyWind> agregator(List<WeatherSnapshot> snapshotList) {
        List<WindPoint> windPointList = windExtractor.extractWindPoints(snapshotList);
        return calculateDailyPresure(windPointList);
    }

    private List<DailyWind> calculateDailyPresure(List<WindPoint> points) {
        Map<LocalDate, List<WindPoint>> grouped = points.stream().
                collect(Collectors.groupingBy(
                        point -> point.getDateTime().toLocalDate()
                ));

        return grouped.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();

                    double effectiveSpeedDay = (int) entry.getValue().stream()
                            .filter(this::isDay)
                            .mapToDouble(point -> determineWind(point.getWindGust(), point.getWindSpeed()))
                            .average()
                            .orElse(0);
                    double effectiveSpeedNight = (int) entry.getValue().stream()
                            .filter(this::isNight)
                            .mapToDouble(point -> determineWind(point.getWindGust(), point.getWindSpeed()))
                            .average()
                            .orElse(0);

                    Integer averageDirectionDay = calculateAverageDirection(entry.getValue().stream()
                            .filter(this::isDay)
                            .toList());
                    Integer averageDirectionNight = calculateAverageDirection(entry.getValue().stream()
                            .filter(this::isNight)
                            .toList());

                    return new DailyWind(date, effectiveSpeedDay, effectiveSpeedNight, averageDirectionDay, averageDirectionNight);
                }).sorted(Comparator.comparing(DailyWind::getDate)).toList();
    }

    private boolean isDay(WindPoint point) {
        int hour = point.getDateTime().getHour();
        return hour > 6 && hour < 21;
    }

    private boolean isNight(WindPoint point) {
        int hour = point.getDateTime().getHour();
        return hour >= 21 || hour <= 6;
    }

    private Integer calculateAverageDirection(List<WindPoint> points) {
        if (points.isEmpty()) {
           return null;
        }

        double sin = points.stream()
                .mapToDouble(p -> Math.sin(Math.toRadians(p.getWindDirection())))
                .sum();

        double cos = points.stream()
                .mapToDouble(p -> Math.cos(Math.toRadians(p.getWindDirection())))
                .sum();

        double angle = Math.toDegrees(Math.atan2(sin, cos));

        if (angle < 0) {
            angle += 360;
        }

        return (int) Math.round(angle);
    }

    private Double determineWind(double windGust, double windSpeed) {
        return windSpeed * 0.3 + windGust * 0.7;
    }

}
