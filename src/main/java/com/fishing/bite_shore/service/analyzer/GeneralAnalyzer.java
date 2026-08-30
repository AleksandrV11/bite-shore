package com.fishing.bite_shore.service.analyzer;

import com.fishing.bite_shore.model.analysis.precipitation.PrecipitationLevel;
import com.fishing.bite_shore.model.analysis.precipitation.PrecipitationState;
import com.fishing.bite_shore.model.analysis.pressure.PressureLevel;
import com.fishing.bite_shore.model.analysis.pressure.PressureMovement;
import com.fishing.bite_shore.model.analysis.pressure.PressureState;
import com.fishing.bite_shore.model.analysis.pressure.PressureTrend;
import com.fishing.bite_shore.model.analysis.temperature.HeatLevel;
import com.fishing.bite_shore.model.analysis.temperature.TemperatureState;
import com.fishing.bite_shore.model.analysis.temperature.WaterTrend;
import com.fishing.bite_shore.model.analysis.wind.Wind;
import com.fishing.bite_shore.model.analysis.wind.WindDirection;
import com.fishing.bite_shore.model.analysis.wind.WindState;
import com.fishing.bite_shore.model.analysis.wind.WindStrength;
import com.fishing.bite_shore.model.season.Season;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.Optional;

@Service

public class GeneralAnalyzer {


    public StringBuilder analyzer(PressureState pressureState, TemperatureState temperatureState, WindState windState, PrecipitationState precipitationState, LocalDateTime date) {
        StringBuilder finalAnswer = new StringBuilder();
        finalAnswer.append(analyzeSeasonAndRecommend(date))
                .append(getAnalizePressure(pressureState))
                .append(getTemperatureAnalysis(temperatureState))
                .append(getWindAnalysis(windState))
                .append(getPrecipitationAnalysis(precipitationState, date));

        return finalAnswer;
    }

    private StringBuilder analyzeSeasonAndRecommend(LocalDateTime date) {

        StringBuilder result = new StringBuilder();
        MonthDay day = MonthDay.from(date);
        Season season = analyzeSeason(date);

        result.append("Рибалка відбувається: ");

        if (season == Season.EARLY_SPRING) {
            result.append("ранньою весною. Тому риба знаходиться ще на глибині.\n");
        } else if (season == Season.SPRING) {
            result.append("навесні. Тому риба знаходиться ще на глибині і в сонячні дні бувають виходи на неглибокі " + "ділянки водойми.\n");
        } else if (season == Season.EARLY_SUMMER) {
            result.append("раннім літом. Риба починає активно рухатися і все частіше її можна знайти на неглибоких " + "участках водойми.\n");
        } else if (season == Season.SUMMER) {
            result.append("влітку. Риба активно харчується.\n");
        } else if (season == Season.EARLY_AUTUMN) {
            result.append("ранньою осінню. Риба активно харчується.\n");
        } else if (season == Season.AUTUMN) {
            result.append("восені. Риба починає скочуватися на глибини, але в сонячні дні можливі виходи на неглибокі" + " ділянки.\n");
        } else if (season == Season.LATE_AUTUMN) {
            result.append("пізньої осені. Риба скотилась на глибину.\n");
        } else {
            result.append("період не аналізується.\n");
        }

        return result;
    }

    private StringBuilder getAnalizePressure(PressureState pressureState) {

        StringBuilder pressure = new StringBuilder();

        if (pressureState.getStartPressure() != 0) {

            pressure.append("На початку аналізованого періоду перед рибалкою тиск був ")
                    .append(pressureState.getStartPressure())
                    .append(", при нормальному тиску для цієї місцевості ")
                    .append(pressureState.getNormPressure())
                    .append(".\n");

            pressure.append(getPressureLevelDescription(pressureState.getPressureLevel()));
        }

        if (pressureState.getPressureTrend() == PressureTrend.NOT_DATA) {
            pressure.append("Даних по тиску за день до рибалки немає.\n");
        } else {
            pressure.append("Наприкінці періоду він став ")
                    .append(pressureState.getEndPressure())
                    .append(", що свідчить про те, що : ")
                    .append(getPressureLevelDescription(pressureState.getPressureLevel()));

            pressure.append("Зміна тиску протягом останньої доби: ");
            if (pressureState.getPressureTrend() == PressureTrend.FALLING) {
                pressure.append("Тиск падає.\n");
            } else if (pressureState.getPressureTrend() == PressureTrend.RISING) {
                pressure.append("Тиск зростає.\n");
            } else {
                if (pressureState.getPressureLevel() == PressureLevel.HIGH) {
                    pressure.append("Тиск сформувався на високому рівні.\n");
                } else if (pressureState.getPressureLevel() == PressureLevel.LOW) {
                    pressure.append("Тиск сформувався на низькому рівні.\n");
                } else {
                    pressure.append("Тиск сформувався на нормальному рівні.\n");
                }
            }

        }

        return pressure;
    }


    private StringBuilder getTemperatureAnalysis(TemperatureState temperature) {

        StringBuilder result = new StringBuilder();
        if (temperature.getFarPeriodDays() == 0) {
            result.append("Данних по температурі за 3 дні до рибалки намає.\n ");
        } else {
            result.append("Температура верхнього шару води за дальній період БЕЗ ВПЛИВУ ВІТРА І ОПАДІВ приблизно ")
                    .append(temperature.getTemperatureFarPeriod().getUpperLayerState().getHeadTemp())
                    .append(" градусів,\n").append("тобто верхній шар в дальньому періоді ")
                    .append(getTemperatureWaterLevel(temperature.getTemperatureFarPeriod().getUpperLayerState().getHeatLevel()));

            result.append("В дальньому періоді зміна температури верхнього шару склала ")
                    .append(temperature.getTemperatureFarPeriod().getUpperLayerState().getHeadTrend())
                    .append(" градусів ");
            if (temperature.getTemperatureFarPeriod().getUpperLayerState().getTemperatureTrend() == WaterTrend.RISING) {
                result.append("це означає що температура підвищується.\n");
            } else if (temperature.getTemperatureFarPeriod().getUpperLayerState().getTemperatureTrend() == WaterTrend.STABLE) {
                result.append("це означає що температура стабільна.\n");
            } else {
                result.append("це означає що температура поніжається.\n");
            }
            result.append("Температура середнього шару води в дальньому періоді приблизно ")
                    .append(temperature.getTemperatureFarPeriod().getMiddleLayerState().getHeadTemp())
                    .append(" градусів,\n").append("тобто середній шар ")
                    .append(getTemperatureWaterLevel(temperature.getTemperatureFarPeriod().getMiddleLayerState().getHeatLevel()));

            result.append("В дальньому періоді зміна температури середнього шару склала ")
                    .append(temperature.getTemperatureFarPeriod().getMiddleLayerState().getHeadTrend())
                    .append(" градусів ");
            if (temperature.getTemperatureFarPeriod().getMiddleLayerState().getTemperatureTrend() == WaterTrend.RISING) {
                result.append("це означає що температура підвищується.\n");
            } else if (temperature.getTemperatureFarPeriod().getMiddleLayerState().getTemperatureTrend() == WaterTrend.STABLE) {
                result.append("це означає що температура стабільна.\n");
            } else {
                result.append("це означає що температура поніжається.\n");
            }
        }
        if (temperature.getNearPeriodDays() == 0) {
            result.append("Данних по температурі за день до рибалки намає.\n ");
        } else {
            result.append("Температура верхнього шару води за день до рибалки приблизно ")
                    .append(temperature.getTemperatureNearPeriod().getUpperLayerState().getHeadTemp())
                    .append(" градусів,\n").append("тобто верхній шар ")
                    .append(getTemperatureWaterLevel(temperature.getTemperatureFarPeriod().getUpperLayerState().getHeatLevel()));

            result.append("Температура середнього шару води за день до рибалки приблизно ")
                    .append(temperature.getTemperatureFarPeriod().getMiddleLayerState().getHeadTemp())
                    .append(" градусів,\n").append("тобто середній шар ")
                    .append(getTemperatureWaterLevel(temperature.getTemperatureFarPeriod().getMiddleLayerState().getHeatLevel()));
        }

        return result;
    }

    private StringBuilder getWindAnalysis(WindState wind) {

        StringBuilder result = new StringBuilder();

        result.append("Вітер аналізується у двох часових проміжках :\n")
                .append("1. Дальній проміжок (4-2 дні) до рибалкі;\n")
                .append("2. Ближній проміжок (1 день ) до рибалкі.\n")
                .append("У дальньому проміжку вітер був: ")
                .append(getDirectory(wind.getFarWind().getWindDirection()));

        result.append("А по силі він був ")
                .append(wind.getFarWind().getStrenght())
                .append(" м/c. ").append("Це: ")
                .append(getStrength(wind.getFarWind()
                        .getWindStrength()));

        Optional.ofNullable(getEffectOnHeadLayers(wind.getFarWind().getWindDirection(), wind.getFarWind().getStrenght()))
                .ifPresent(result::append);

        Optional.ofNullable(getEffectiveMiddleLayer(wind.getFarWind().getWindDirection(), wind.getFarWind().getStrenght()))
                .ifPresent(result::append);

        result.append("За день до рибалкі вітер був: ")
                .append(getDirectory(wind.getNearWind().getWindDirection()));
        result.append("А сила його була ")
                .append(wind.getNearWind().getStrenght())
                .append(" м/c. ").append("Це: ")
                .append(getStrength(wind.getNearWind().getWindStrength()));

        if (getEffectOnHeadLayers(wind.getNearWind().getWindDirection(), wind.getNearWind().getStrenght()) != null) {
            result.append("Також за день до рибалкі - ")
                    .append(getEffectOnHeadLayers(wind.getNearWind().getWindDirection(), wind.getNearWind().getStrenght()));
        }

        result.append("Дивлячись за напрямком вітру у дальньому періоді то кормова база зараз це : ")
                .append(determineFishingSide(wind.getFarWind().getWindDirection()))
                .append(".\n");
        result.append("А за день до рибалкі кормова база може почати зміщуватись -> на : ")
                .append(determineFishingSide(wind.getNearWind().getWindDirection()))
                .append(".\n");

        Optional.ofNullable(coldOrStrongWind(wind)).ifPresent(result::append);

        return result;
    }


    private StringBuilder getPrecipitationAnalysis(PrecipitationState precipitation, LocalDateTime date) {

        StringBuilder result = new StringBuilder();

        if (precipitation.getDayMinus2() != null && precipitation.getDayMinus2().getPrecipitation() > 0) {
            result.append("За 2 дні до рибалки були: ");
            result.append(getPrecipitationDescription(precipitation.getDayMinus2().getLevel()));
            if (analyzeSeason(date) == Season.SUMMER) {
                result.append("Влітку коли спека, опади збагачують киснем воду і риба " + "стає активнішою.\n ");
            }
        }
        if ((precipitation.getDayMinus1() != null && precipitation.getDayMinus1().getPrecipitation() > 0)) {
            result.append("За день до рибалкі були : ");
            result.append(getPrecipitationDescription(precipitation.getDayMinus1().getLevel()));
            if (analyzeSeason(date) == Season.SUMMER) {
                result.append("Опади збагачують киснем воду і риба " + "стає активніша.\n ");
            }
        }

        return result;
    }

    private String getPrecipitationDescription(PrecipitationLevel precipitation) {
        if (precipitation == PrecipitationLevel.LIGHT) {
            return "Слабкі опади. Вони могли охолодити верхній шар.\n";
        } else if (precipitation == PrecipitationLevel.MODERATE) {
            return "Помірні опади. Вони могли охолодили верхній шар.\n";
        } else {
            return "Сильні опади. Вони могли охолодити верхній шар на 3 градуси.\n";
        }
    }

    public Season analyzeSeason(LocalDateTime date) {

        MonthDay day = MonthDay.from(date);

        if (isBetween(day, MonthDay.of(3, 1), MonthDay.of(4, 15))) {
            return Season.EARLY_SPRING;
        } else if (isBetween(day, MonthDay.of(4, 15), MonthDay.of(6, 1))) {
            return Season.SPRING;
        } else if (isBetween(day, MonthDay.of(6, 1), MonthDay.of(6, 15))) {
            return Season.EARLY_SUMMER;
        } else if (isBetween(day, MonthDay.of(6, 15), MonthDay.of(9, 1))) {
            return Season.SUMMER;
        } else if (isBetween(day, MonthDay.of(9, 1), MonthDay.of(9, 20))) {
            return Season.EARLY_AUTUMN;
        } else if (isBetween(day, MonthDay.of(9, 20), MonthDay.of(10, 5))) {
            return Season.AUTUMN;
        } else if (isBetween(day, MonthDay.of(10, 5), MonthDay.of(11, 10))) {
            return Season.LATE_AUTUMN;
        } else {
            return Season.NOT_ANALYZED;
        }
    }

    private boolean isBetween(MonthDay date, MonthDay start, MonthDay end) {
        return !date.isBefore(start) && date.isBefore(end);
    }

    private StringBuilder coldOrStrongWind(WindState wind) {

        StringBuilder result = new StringBuilder();

        if ((wind.getFarWind().getWindDirection() == WindDirection.N || wind.getFarWind().getWindDirection() == WindDirection.NE || wind.getFarWind().getWindDirection() == WindDirection.NW) && wind.getFarWind().getStrenght() > 7) {
            result.append("Стосовно спокою від вітру у дальньму періоді це берег : ").append(getDirectory(wind.getFarWind().getWindDirection())).append(".\n");
        }
        if (((wind.getNearWind().getWindDirection() == WindDirection.N || wind.getNearWind().getWindDirection() == WindDirection.NE || wind.getNearWind().getWindDirection() == WindDirection.NW) && wind.getNearWind().getStrenght() > 7) || wind.getNearWind().getStrenght() > 9) {
            result.append("Стосовно спокою від вітру у короткому періоді риба може перемещатись на берег : ").append(getDirectory(wind.getNearWind().getWindDirection())).append(".\n");
        }

        return result;
    }

    private String determineFishingSide(WindDirection windDirection) {
        if (windDirection == WindDirection.N) {
            return "Південь";
        } else if (windDirection == WindDirection.NE) {
            return "Південний захід";
        } else if (windDirection == WindDirection.E) {
            return "Захід";
        } else if (windDirection == WindDirection.SE) {
            return "Північний захід";
        } else if (windDirection == WindDirection.S) {
            return "Північ";
        } else if (windDirection == WindDirection.SW) {
            return "Північний схід";
        } else if (windDirection == WindDirection.W) {
            return "Схід";
        } else if (windDirection == WindDirection.NW) {
            return "Південний схід";
        } else {
            return "Даних немає";
        }
    }


    private String getEffectOnHeadLayers(WindDirection windDirection, int strenght) {
        if ((windDirection == WindDirection.N || windDirection == WindDirection.NE || windDirection == WindDirection.NW)) {
            if (strenght > 4) {
                return "Оскільки вітер був близько -> Пінічний і його сила була більше 4 то вітер охолонив " + "верхній шар.\n";
            }
        }
        if ((windDirection == WindDirection.S || windDirection == WindDirection.SE || windDirection == WindDirection.SW)) {
            if (strenght > 5) {
                return "Оскільки вітер був близько -> Південний і його сила була слабкіша за 8 то вітер посприяв" + " підвищенню температури верхнього шару.\n";
            }
        }

        return null;
    }

    private String getEffectiveMiddleLayer(WindDirection windDirection, int strenght) {
        if ((windDirection == WindDirection.N || windDirection == WindDirection.NE || windDirection == WindDirection.NW)) {
            if (strenght > 7) {
                return "Оскільки вітер був близько -> Пінічний і його сила була більше 7 то вітер міг вплинути " + "на середній шар охолоджуючи його.\n";
            }
            if ((windDirection == WindDirection.S || windDirection == WindDirection.SE || windDirection == WindDirection.SW)) {
                if (strenght < 8) {
                    return "Оскільки вітер був близько -> Південний і його сила була не більше 8 то вітер міг " + "вплинути " + "на середній шар підігрівше його.\n";
                }
            }
        }
        return null;
    }

    private String getStrength(WindStrength windStrength) {
        if (windStrength == WindStrength.CALM) {
            return "Штиль / дуже слабкий вітер.\n";
        } else if (windStrength == WindStrength.LIGHT) {
            return "Легкий вітер.\n";
        } else if (windStrength == WindStrength.MODERATE) {
            return "Помірний вітер.\n";
        } else if (windStrength == WindStrength.STRONG) {
            return "Сильний вітер.\n";
        } else if (windStrength == WindStrength.VERY_STRONG) {
            return "Дуже сильний вітер.\n";
        } else {
            return "Даних немає.\n";
        }
    }

    private String getDirectory(WindDirection windDirection) {
        if (windDirection == WindDirection.N) {
            return "Північний. ";
        } else if (windDirection == WindDirection.NE) {
            return "Північно - східний. ";
        } else if (windDirection == WindDirection.E) {
            return "Східний. ";
        } else if (windDirection == WindDirection.SE) {
            return "Південно - східний. ";
        } else if (windDirection == WindDirection.S) {
            return "Південно - східний. ";
        } else if (windDirection == WindDirection.SW) {
            return "Південно - західний. ";
        } else if (windDirection == WindDirection.W) {
            return "Західний. ";
        } else if (windDirection == WindDirection.NW) {
            return "Північно - західний. ";
        } else {
            return "Данних немає. ";
        }
    }

    private String getTemperatureWaterLevel(HeatLevel heatLevel) {
        if (heatLevel.equals(HeatLevel.COLD)) {
            return "має холодну воду ( < 14 градусів ).\nОбмін речовин у риби сповільнений.\n";
        } else if (heatLevel.equals(HeatLevel.COOL)) {
            return "має прохолодну воду ( 14 - 17 градусів ).\nАктивність є, але нижча від оптимальної.\n";
        } else if (heatLevel.equals(HeatLevel.COMFORT)) {
            return "має комфортну воду ( 17 - 27 градусів ).\nНайбільш сприятлива температурна зона для активного " +
                    "харчування.\n";
        } else if (heatLevel.equals(HeatLevel.WARM)) {
            return "має теплу воду ( 27 - 30 градусів ).\nАктивний обмін речовин, але треба враховувати кисень.\n";
        } else {
            return "має перегріту воду ( > 30 градусів ).\nТемпература вже може негативно впливати.\n";
        }
    }

    private String getPressureLevelDescription(PressureLevel pressureLevel) {
        if (pressureLevel == PressureLevel.LOW) {
            return "Тиск був занизький.\n";
        } else if (pressureLevel == PressureLevel.NORMAL) {
            return "Тиск був у межах норми.\n";
        } else if (pressureLevel == PressureLevel.HIGH) {
            return "Тиск був високий.\n";
        }

        return "Даних по тиску за аналізований період немає.\n";
    }


}

