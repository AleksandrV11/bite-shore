package com.fishing.bite_shore.service.aggregator;

import com.fishing.bite_shore.extractor.TemperatureExtractor;
import com.fishing.bite_shore.model.daily.DailyTemperature;
import com.fishing.bite_shore.model.point.TemperaturePoint;
import com.fishing.bite_shore.model.WeatherSnapshot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TemperatureAggregator {
    private final TemperatureExtractor temperatureExtractor;

    public List<DailyTemperature> agregator (List<WeatherSnapshot> snapshotList) {
        List<TemperaturePoint> temperaturePointList = temperatureExtractor.extractTemperaturePoints(snapshotList);

        return calculateDailyTemperature(temperaturePointList);
    }

    private List<DailyTemperature> calculateDailyTemperature(List<TemperaturePoint> points) {
        Map<LocalDate, List<TemperaturePoint>> grouped = points.stream()
                .collect(Collectors.groupingBy(
                        point -> point.getDateTime().toLocalDate()
                ));

        return grouped.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();

                    OptionalDouble dayAverage = entry.getValue().stream()
                            .filter(this::isDay)
                            .mapToDouble(TemperaturePoint::getTemperature)
                            .average();

                    OptionalDouble nightAverage = entry.getValue().stream()
                            .filter(this::isNight)
                            .mapToDouble(TemperaturePoint::getTemperature)
                            .average();

                    Double day = dayAverage.isPresent()
                            ? dayAverage.getAsDouble()
                            : null;

                    Double night = nightAverage.isPresent()
                            ? nightAverage.getAsDouble()
                            : null;

                    return new DailyTemperature(date, day, night);
                })
                .sorted(Comparator.comparing(DailyTemperature::getLocalDate))
                .toList();
    }

    private boolean isDay(TemperaturePoint point) {
        int hour = point.getDateTime().getHour();
        return hour > 6 && hour < 21;
    }

    private boolean isNight(TemperaturePoint point) {
        int hour = point.getDateTime().getHour();
        return hour >= 21 || hour <= 6;
    }
}



