package com.fishing.bite_shore.service.aggregator;

import com.fishing.bite_shore.extractor.PrecipitationExtractor;
import com.fishing.bite_shore.model.daily.DailyPrecipitation;
import com.fishing.bite_shore.model.point.PrecipitationPoint;
import com.fishing.bite_shore.model.WeatherSnapshot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PrecipitationAggregator {
    private final PrecipitationExtractor precipitationExtractor;


    public List<DailyPrecipitation> agregator(List<WeatherSnapshot> snapshotList) {
        List<PrecipitationPoint> precipitationPointList = precipitationExtractor.extractPrecipitationPoints(snapshotList);
        return calculateDailyPrecipitation(precipitationPointList);
    }


    private List<DailyPrecipitation> calculateDailyPrecipitation(List<PrecipitationPoint> points) {
        Map<LocalDate, List<PrecipitationPoint>> grouped = points.stream().
                collect(Collectors.groupingBy(
                        point -> point.getDateTime().toLocalDate()
                ));
        return grouped.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    double maxPrecipitationDay = entry.getValue().stream()
                            .filter(this::isDay)
                            .mapToDouble(PrecipitationPoint::getPrecipitation)
                            .max()
                            .orElse(0);
                    double maxPrecipitationNight = entry.getValue().stream()
                            .filter(this::isNight)
                            .mapToDouble(PrecipitationPoint::getPrecipitation)
                            .max()
                            .orElse(0);
                    int maxPopPercentDay = entry.getValue().stream()
                            .filter(this::isDay)
                            .mapToInt(PrecipitationPoint::getPopPercent)
                            .max()
                            .orElse(0);
                    int maxPopPercentNight = entry.getValue().stream()
                            .filter(this::isDay)
                            .mapToInt(PrecipitationPoint::getPopPercent)
                            .max()
                            .orElse(0);
                    return new DailyPrecipitation(date, maxPrecipitationDay, maxPrecipitationNight, maxPopPercentDay,
                            maxPopPercentNight);
                }).sorted(Comparator.comparing(DailyPrecipitation::getLocalDate)).toList();
    }

    private boolean isDay(PrecipitationPoint point) {
        int hour = point.getDateTime().getHour();
        return hour > 6 && hour < 21;
    }

    private boolean isNight(PrecipitationPoint point) {
        int hour = point.getDateTime().getHour();
        return hour >= 21 || hour <= 6;
    }


}
