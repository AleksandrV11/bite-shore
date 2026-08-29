package com.fishing.bite_shore.service.analyzer;

import com.fishing.bite_shore.exception.InvalidPrecipitationException;
import com.fishing.bite_shore.model.analysis.precipitation.Precipitation;
import com.fishing.bite_shore.model.analysis.precipitation.PrecipitationLevel;
import com.fishing.bite_shore.model.analysis.precipitation.PrecipitationState;
import com.fishing.bite_shore.model.daily.DailyWeatherData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrecipitationAnalyser {

    public PrecipitationState analyze(List<DailyWeatherData> dailyWeatherData, LocalDate localDate) {

        List<LocalDate> dateList = dailyWeatherData.stream()
                .map(DailyWeatherData::getLocalDate)
                .sorted()
                .toList();

        List<Integer> precipitationList = dailyWeatherData.stream()
                .map(DailyWeatherData::getDailyPrecipitation)
                .mapToInt(a -> (int) Math.max(
                        a.getMaxPrecipitationDay(),
                        a.getMaxPrecipitationNight()
                ))
                .boxed()
                .toList();

        List<Integer> popPercentList = dailyWeatherData.stream()
                .map(DailyWeatherData::getDailyPrecipitation)
                .mapToInt(a -> (a.getMaxPopPercentDay() + a.getMaxPopPercentNight()) / 2)
                .boxed()
                .toList();

        if (precipitationList.isEmpty()) {
            throw new InvalidPrecipitationException("Даних по дощу немає");
        }

        Precipitation precipitationDayMinus2 = null;
        Precipitation precipitationDayMinus1 = null;

        int indexDayMinus1 = dateList.indexOf(localDate.minusDays(1));
        int indexDayMinus2 = dateList.indexOf(localDate.minusDays(2));

        if (indexDayMinus1 >= 0) {
            int precipitation = precipitationList.get(indexDayMinus1);

            precipitationDayMinus1 = new Precipitation(
                    precipitation,
                    determinePrecipitationLevel(precipitation),
                    popPercentList.get(indexDayMinus1)
            );
        }

        if (indexDayMinus2 >= 0) {
            int precipitation = precipitationList.get(indexDayMinus2);

            precipitationDayMinus2 = new Precipitation(
                    precipitation,
                    determinePrecipitationLevel(precipitation),
                    popPercentList.get(indexDayMinus2)
            );
        }

        return new PrecipitationState(
                precipitationDayMinus2,
                precipitationDayMinus1
        );
    }

    private PrecipitationLevel determinePrecipitationLevel(int precipitation) {

        if (precipitation == 0) {
            return PrecipitationLevel.NONE;
        } else if (precipitation < 5) {
            return PrecipitationLevel.LIGHT;
        } else if (precipitation < 15) {
            return PrecipitationLevel.MODERATE;
        } else {
            return PrecipitationLevel.HEAVY;
        }
    }


}