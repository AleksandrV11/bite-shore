package com.fishing.bite_shore.dto.dtoBase;

import lombok.Data;

import java.util.List;

@Data
public class DTO {
    private String cod;
    private int message;
    private int cnt;
    private List<WinList> list;
    private City city;
}












