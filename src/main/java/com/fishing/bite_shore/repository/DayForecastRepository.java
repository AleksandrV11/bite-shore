package com.fishing.bite_shore.repository;

import com.fishing.bite_shore.entity.DayForecastEntity;
import com.fishing.bite_shore.weatherData.DayForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface DayForecastRepository extends JpaRepository<DayForecastEntity,Long> {

}
