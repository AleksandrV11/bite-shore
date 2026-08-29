package com.fishing.bite_shore.repository;

import com.fishing.bite_shore.entity.WeatherDataEntity;
import com.fishing.bite_shore.model.WeatherSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeatherDataRepository extends JpaRepository<WeatherDataEntity, Long> {

    Optional<WeatherDataEntity> findByLocationIdAndForecastTime(Long locationId, LocalDateTime dateTime);
    @Query("""
    select new com.fishing.bite_shore.model.WeatherSnapshot(
        w.forecastTime,
        w.temperature,
        w.pressure,
        w.humidity,
        w.windSpeed,
        w.windDirection,
        w.windGust,
        w.precipitation,
        w.popPercent
    )
    from WeatherDataEntity w
    where w.location.id = :locationId
    order by w.forecastTime
    """)
    List<WeatherSnapshot> findSnapshotsByLocationId(@Param("locationId") Long locationId);
}
