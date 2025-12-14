package com.fishing.bite_shore.daysForAnalysis;

import com.fishing.bite_shore.weatherData.DayForecast;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
public class OneDayAnalysis {
    private DayForecast dayForecast;

//    public OneDayAnalysis(DayForecast dayForecast) {
//        this.dayForecast = dayForecast;
//    }
}
