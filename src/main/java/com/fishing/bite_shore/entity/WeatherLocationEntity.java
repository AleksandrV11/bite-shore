package com.fishing.bite_shore.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Entity
@Table(name = "weather_location")
public class WeatherLocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "city_id")
    private Long externalId;
    @Column(name = "city_name")
    private String name;

    //час отримання інфи
    @Column(columnDefinition = "timestamp(0)")
    private LocalDateTime receivedAt;
    //потрібно замапіть с переводом в время
    private LocalDateTime sunrise;
    private LocalDateTime sunset;
    //висота над рівнем моря
    @Column(name = "elevation")
    private Double elevation;
    // Class CoordDTO

    private double lat;
    private double lon;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeatherDataEntity> weatherData = new ArrayList<>();

    public void addWeatherData(WeatherDataEntity data) {
        weatherData.add(data);
        data.setLocation(this);
    }


}
