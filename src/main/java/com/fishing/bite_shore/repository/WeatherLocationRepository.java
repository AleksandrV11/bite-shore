package com.fishing.bite_shore.repository;

import com.fishing.bite_shore.entity.WeatherLocationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WeatherLocationRepository extends JpaRepository<WeatherLocationEntity, Long> {

    Optional <WeatherLocationEntity> findByExternalId(Long externalId);
}

