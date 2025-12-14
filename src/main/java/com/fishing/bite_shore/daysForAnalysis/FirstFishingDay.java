package com.fishing.bite_shore.daysForAnalysis;

import com.fishing.bite_shore.weatherData.DayForecast;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
public class FirstFishingDay {
    private DayForecast dayForecast;

//    public FirstFishingDay(DayForecast dayForecast) {
//        this.dayForecast = dayForecast;
//    }
}
