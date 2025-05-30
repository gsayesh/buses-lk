package lk.buses.bus.application.mapper;

import lk.buses.bus.application.dto.request.*;
import lk.buses.bus.application.dto.response.*;
import lk.buses.bus.domain.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OperatorMapper.class})
public interface BusMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operator", ignore = true)
    @Mapping(target = "serviceCategory", ignore = true)
    @Mapping(target = "hasGpsDevice", constant = "false")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    @Mapping(target = "trackingHistory", ignore = true)
    Bus toEntity(RegisterBusRequest request);

    BusResponse toResponse(Bus bus);

    List<BusResponse> toResponseList(List<Bus> buses);
}