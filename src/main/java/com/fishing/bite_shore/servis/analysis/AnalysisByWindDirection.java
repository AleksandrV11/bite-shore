package com.fishing.bite_shore.servis.analysis;

import com.fishing.bite_shore.entity.DayForecastEntity;
import com.fishing.bite_shore.weatherData.weather.Wind;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class AnalysisByWindDirection {
    public List<Integer> analysisWindDirection(List<Map<LocalDateTime, Wind>> mapList) {

        for (Map<LocalDateTime, Wind> listMap : mapList) {
            for (Map.Entry<LocalDateTime, Wind> localDateTimeWindEntry : listMap.entrySet()) {

            }
        }



        return null;
    }
    //  private final SortByCoordinates sortByCoordinates;
//             ПОКИ ВСЕ НЕПОТРІБ ЯК ІДЕЯ
//    // отримуємо лист по черзі звідки дує в градусах
//    public List<Integer> findWindDirection(List<DayForecastEntity> dayForecastEntities) {
//        return dayForecastEntities.stream()
//                .flatMap(a -> Stream.of(
//                        a.getMorning(),
//                        a.getAfternoon(),
//                        a.getEvening(),
//                        a.getNight()
//                ))
//                .filter(Objects::nonNull)          // прибираємо null-часи
//                .map(a -> a.getWind())   // дістаємо вітер
//                .filter(Objects::nonNull)          // прибираємо null-вітри
//                .map(a -> a.getWindDirection())       // дістаємо напрямок
//                .filter(Objects::nonNull)          // прибираємо null-напрямки
//                .toList();
//    }
//
//     //розбиваэм по 2 та рахуєм середнє
//    public List<Integer> groupMidDirection(List<Integer> windDirection) {
//        List<Integer> listAverage = new ArrayList<>();
//        for (int i = 0; i < windDirection.size(); i += 2) {
//            // якщо залишився один останній елемент — додаємо його як є
//            if (i == windDirection.size() - 1 && windDirection.size() % 2 != 0) {
//                listAverage.add(windDirection.get(i));
//                break;
//            }
//            Integer average = calculateAverageWindDirections(windDirection.get(i), windDirection.get(i + 1));
//            listAverage.add(average);
//        }
//        return listAverage;
//    }
//         //кругова
//    public Integer calculateAverageWindDirections(Integer a, Integer b) {
//        double radA = Math.toRadians(a);
//        double radB = Math.toRadians(b);
//
//        double x = Math.cos(radA) + Math.cos(radB);
//        double y = Math.sin(radA) + Math.sin(radB);
//
//        double meanRad = Math.atan2(y, x);
//        double meanDeg = Math.toDegrees(meanRad);
//        return (int) Math.round((meanDeg + 360) % 360);
//    }
}


