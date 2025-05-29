package lk.buses.route.application.mapper;

import lk.buses.route.application.dto.request.*;
import lk.buses.route.application.dto.response.*;
import lk.buses.route.domain.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "segmentTimes", ignore = true)
    Route toEntity(CreateRouteRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RouteStop toEntity(CreateRouteStopRequest request);

    RouteResponse toResponse(Route route);

    RouteStopResponse toResponse(RouteStop stop);

    List<RouteResponse> toResponseList(List<Route> routes);
}