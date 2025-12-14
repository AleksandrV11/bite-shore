package com.fishing.bite_shore.mapper;

import com.fishing.bite_shore.dto.dtoBase.City;
import com.fishing.bite_shore.dto.dtoBase.Coord;
import com.fishing.bite_shore.dto.dtoBase.WinList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoordMapper {
    @Mapping(source = "lat", target = "lat")
    @Mapping(source = "lon", target = "lon")
    Coord toCoord(Coord coordDto);

}
