package com.fishing.bite_shore.repository;

import com.fishing.bite_shore.entity.WeatherDataEntity;
import com.fishing.bite_shore.weatherData.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDataRepository extends JpaRepository<WeatherDataEntity, Long> {
}
