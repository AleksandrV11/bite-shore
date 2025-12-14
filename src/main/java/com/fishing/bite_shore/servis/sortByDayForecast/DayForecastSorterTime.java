package com.fishing.bite_shore.servis.sortByDayForecast;

import com.fishing.bite_shore.entity.DayForecastEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@AllArgsConstructor

public class DayForecastSorterTime {
    public List<DayForecastEntity> getSortedDayForecast(List<DayForecastEntity> dayForecastEntities) {
        List<DayForecastEntity> sortedDays = dayForecastEntities.stream()
                .sorted(Comparator.comparing(day -> Stream.of(day.getMorning(), day.getAfternoon(), day.getEvening(), day.getNight())
                        .filter(Objects::nonNull)
                        .map(b->b.getLocalDateTime())
                        .min(LocalDateTime::compareTo)
                        .orElse(LocalDateTime.MIN)))
                .toList();
        return sortedDays;
    }

}
