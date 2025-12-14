package com.fishing.bite_shore.daysForAnalysis;

import com.fishing.bite_shore.weatherData.DayForecast;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
public class ThirdDayAnalysis {
    private DayForecast dayForecast;

//    public ThirdDayAnalysis(DayForecast dayForecast) {
//        this.dayForecast = dayForecast;
//    }
}
