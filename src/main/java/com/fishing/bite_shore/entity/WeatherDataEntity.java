package com.fishing.bite_shore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "weather_data")
public class WeatherDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //winListDto
    private LocalDateTime forecastTime;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private WeatherLocationEntity location;
    @Column(name = "elevation")
    private Double elevation;
    //майн
    private double temperature;
    private int pressure;
    private int humidity;

    //  вітер
    private double windSpeed;
    private int windDirection;
    private double windGust;

    //опади
    @Column(name = "rain_precipitation")
    private int precipitation;
    //winListDto вірогідність опадів
    @Column(name = "rain_pop")
    private int popPercent;

    //опис погоди WeatherDTO
    private int weatherCode;
    @Column(name = "weather_group")
    private String weatherGroup;
    private String description;
    @Column(name = "icon_front")
    private String icon;


}
