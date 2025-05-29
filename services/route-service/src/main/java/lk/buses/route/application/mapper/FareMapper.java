package lk.buses.route.application.mapper;

import lk.buses.route.application.dto.response.*;
import lk.buses.route.domain.entity.FareStructure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FareMapper {

    FareStructureResponse toResponse(FareStructure fareStructure);
}