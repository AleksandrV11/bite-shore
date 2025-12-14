package com.fishing.bite_shore.servis.sortByDayForecast;

import com.fishing.bite_shore.entity.DayForecastEntity;
import com.fishing.bite_shore.repository.DayForecastRepository;
import com.fishing.bite_shore.servis.bd.WeatherServiceToBd;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DayForecastSorterRepository {
    private final DayForecastRepository dayForecastRepository;
    private final WeatherServiceToBd weatherServiceToBd;

    // повертає лист данних максимум  за останні 5 днів
    public List<DayForecastEntity> getLastFiveDaysSorted(double lat, double lon, LocalDateTime localDateTime) {
        List<DayForecastEntity> dayForecastEntitySortByCoordinates = getDayForecastEntitySortByCoordinates(lat, lon);
        LocalDateTime fiveDaysBefore = localDateTime.minusDays(5);
        List<DayForecastEntity> lastFiveDaysSorted = dayForecastEntitySortByCoordinates.stream().
                filter(a ->
                        (a.getMorning() != null && checkingSupplyDuringPeriod(a.getMorning().getLocalDateTime(),
                                fiveDaysBefore, localDateTime)) ||
                                (a.getAfternoon() != null && checkingSupplyDuringPeriod(a.getAfternoon().getLocalDateTime(),
                                        fiveDaysBefore, localDateTime)) ||
                                (a.getEvening() != null && checkingSupplyDuringPeriod(a.getEvening().getLocalDateTime(),
                                        fiveDaysBefore, localDateTime)) ||
                                (a.getNight() != null && checkingSupplyDuringPeriod(a.getNight().getLocalDateTime(),
                                        fiveDaysBefore, localDateTime)))
                .toList();
        return lastFiveDaysSorted;
    }

    //перевірка чи попадає дата в період 5 днів
    public boolean checkingSupplyDuringPeriod(LocalDateTime timeDay, LocalDateTime timeFrom, LocalDateTime timeTo) {
        return !timeDay.isBefore(timeFrom) && !timeDay.isAfter(timeTo);
    }

    //відсортований по датам лист із листа сортованого по координатах
    public List<DayForecastEntity> getDayForecastEntitiesSortByTime(double lat, double lon,
                                                                    List<DayForecastEntity> forecastEntities,
                                                                    List<DayForecastEntity> forecastEntitiesSortCoordinates) {
        // Робимо копію, щоб уникнути UnsupportedOperationException і не змінювати оригінал
        List<DayForecastEntity> dayForecastEntitiesSortCoord = new ArrayList<>(forecastEntitiesSortCoordinates);
        for (DayForecastEntity dayNew : forecastEntities) {
            boolean temp = false;
            for (DayForecastEntity daySort : dayForecastEntitiesSortCoord) {
                if (checkByDate(dayNew, daySort)) {
                    daySort.setMorning(dayNew.getMorning());
                    daySort.setAfternoon(dayNew.getAfternoon());
                    daySort.setEvening(dayNew.getEvening());
                    daySort.setNight(dayNew.getNight());
                    temp = true;
                    break;
                }
            }
            if (!temp) {
                dayForecastEntitiesSortCoord.add(dayNew);
            }
        }
        return dayForecastEntitiesSortCoord;
    }

    public boolean checkByDate(DayForecastEntity a, DayForecastEntity b) {
        // Якщо взагалі нічого порівнювати — не співпадають
        if (a == null || b == null) return false;
        // Перевірка morning
        if (a.getMorning() != null && b.getMorning() != null) {
            if (a.getMorning().getLocalDateTime().toLocalDate()
                    .equals(b.getMorning().getLocalDateTime().toLocalDate())) {
                return true;
            }
        }
        // Перевірка afternoon
        if (a.getAfternoon() != null && b.getAfternoon() != null) {
            if (a.getAfternoon().getLocalDateTime().toLocalDate()
                    .equals(b.getAfternoon().getLocalDateTime().toLocalDate())) {
                return true;
            }
        }
        // Перевірка evening
        if (a.getEvening() != null && b.getEvening() != null) {
            if (a.getEvening().getLocalDateTime().toLocalDate()
                    .equals(b.getEvening().getLocalDateTime().toLocalDate())) {
                return true;
            }
        }
        // Перевірка night
        if (a.getNight() != null && b.getNight() != null) {
            if (a.getNight().getLocalDateTime().toLocalDate()
                    .equals(b.getNight().getLocalDateTime().toLocalDate())) {
                return true;
            }
        }
        // Якщо ніде не співпало
        return false;
    }

    // відсортований по відстані лист всіх даних з бд
    public List<DayForecastEntity> getDayForecastEntitySortByCoordinates(double lat, double lon) {
        List<DayForecastEntity> dayForecastEntitiesAll = dayForecastRepository.findAll();
        List<DayForecastEntity> dayForecastEntitiesSortByCoordinates = dayForecastEntitiesAll.stream().filter(a -> {
            double distance = distanceDetermination(a.getCoord().getLat(), a.getCoord().getLon(), lat, lon);
            return checkingZonesByCoordinates(distance);
        }).toList();
        return dayForecastEntitiesSortByCoordinates;
    }

    public boolean checkingZonesByCoordinates(double distance) {
        if (distance > 10) {
            // візуалізація тут зелені а тут жовті
            System.out.println("Точка далі 10 км.(package com.fishing.bite_shore.servis.sortByCoord.checkingZonesByCoordinates)");
            return false;
        }
        System.out.println(" Точка підпадає у зону 10 км.(package com.fishing.bite_shore.servis.sortByCoord.checkingZonesByCoordinates)");// візуалізація
        return true;
    }

    public double distanceDetermination(double latBd, double lonBd, double lat, double lon) {
        double R = 6371; // радіус Землі в км
        double dLat = Math.toRadians(latBd - lat);
        double dLon = Math.toRadians(lonBd - lon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(latBd)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
