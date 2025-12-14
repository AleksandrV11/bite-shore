package com.fishing.bite_shore.dto.dtoBase;

import lombok.Data;

@Data
public class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
}
