package com.fishing.bite_shore.service.aggregator;

import com.fishing.bite_shore.extractor.PressureExtractor;
import com.fishing.bite_shore.model.daily.DailyPressure;
import com.fishing.bite_shore.model.point.PressurePoint;
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
public class PressureAggregator {
    private final PressureExtractor pressureExtractor;

    public List<DailyPressure> agregator(List<WeatherSnapshot> snapshotList) {
        List<PressurePoint> pressurePoints = pressureExtractor.extractPressurePoints(snapshotList);

        return calculateDailyPresure(pressurePoints);
    }

    private List<DailyPressure> calculateDailyPresure(List<PressurePoint> points) {
        Map<LocalDate, List<PressurePoint>> grouped = points.stream()
                .collect(Collectors.groupingBy(
                        point -> point.getDateTime().toLocalDate()
                ));

        return grouped.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();

                    OptionalDouble dayAverage = entry.getValue().stream()
                            .filter(this::isDay)
                            .mapToDouble(PressurePoint::getPressure)
                            .average();

                    OptionalDouble nightAverage = entry.getValue().stream()
                            .filter(this::isNight)
                            .mapToDouble(PressurePoint::getPressure)
                            .average();

                    Integer day = dayAverage.isPresent()
                            ? (int) Math.round(dayAverage.getAsDouble())
                            : null;

                    Integer night = nightAverage.isPresent()
                            ? (int) Math.round(nightAverage.getAsDouble())
                            : null;

                    return new DailyPressure(date, day, night);
                })
                .sorted(Comparator.comparing(DailyPressure::getLocalDate))
                .toList();
    }

    private boolean isDay(PressurePoint point) {
        int hour = point.getDateTime().getHour();
        return hour > 6 && hour < 21;
    }

    private boolean isNight(PressurePoint point) {
        int hour = point.getDateTime().getHour();
        return hour >= 21 || hour <= 6;
    }
}
