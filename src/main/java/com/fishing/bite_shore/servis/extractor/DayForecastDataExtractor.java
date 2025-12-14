package com.fishing.bite_shore.servis.extractor;

import com.fishing.bite_shore.entity.DayForecastEntity;
import com.fishing.bite_shore.weatherData.weather.Wind;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DayForecastDataExtractor {

    public List<Map<LocalDateTime, Wind>> getWindByDateTime(List<DayForecastEntity> dayForecastEntities) {
        List<Map<LocalDateTime, Wind>> windByDateTime = dayForecastEntities.stream()
                .map(day -> Stream.of(
                                day.getMorning(),
                                day.getAfternoon(),
                                day.getEvening(),
                                day.getNight()
                        )
                        .filter(Objects::nonNull) // виключаємо null
                        .collect(Collectors.toMap(
                                a -> a.getLocalDateTime(),  // ключ
                                a -> a.getWind()            // значення
                        )))
                .toList();
        return windByDateTime;
    }
}
