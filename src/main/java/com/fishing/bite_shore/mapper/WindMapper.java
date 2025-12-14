package com.fishing.bite_shore.mapper;

import com.fishing.bite_shore.weatherData.weather.Wind;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface WindMapper {

    @Mapping(source = "deg", target = "windDirection")
    @Mapping(source = "speed", target = "windPower")
    @Mapping(source = "gust", target = "impulseForce")
    Wind toWind(com.fishing.bite_shore.dto.dtoBase.Wind wind);

//    @Named("degToDirection")
//    default String degToDirection(int deg) {
//        if (deg > 338 && deg <= 360) return "N";
//        if (deg >= 0 && deg <= 23) return "N";
//        if (deg > 23 && deg <= 68) return "NE";
//        if (deg > 68 && deg <= 113) return "E";
//        if (deg > 113 && deg <= 158) return "SE";
//        if (deg > 158 && deg <= 203) return "S";
//        if (deg > 203 && deg <= 248) return "SW";
//        if (deg > 248 && deg <= 293) return "W";
//        if (deg > 293 && deg <= 338) return "NW";
//        return " Данні з направлення вітру не валідні ";
//    }
//
//    @Named("speedToWindPower")
//    default int speedToWindPower(double speed) {
//        return (int) Math.round(speed);
//    }
//
//    @Named("gustToImpulseForce")
//    default int gustToImpulseForce(double gust) {
//        return (int) Math.round(gust);
//    }
}
