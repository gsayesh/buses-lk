package lk.buses.bus.application.mapper;

import lk.buses.bus.application.dto.request.CreateOperatorRequest;
import lk.buses.bus.application.dto.response.BusOperatorResponse;
import lk.buses.bus.domain.entity.BusOperator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OperatorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "buses", ignore = true)
    BusOperator toEntity(CreateOperatorRequest request);

    @Mapping(target = "busCount", expression = "java(operator.getBuses() != null ? operator.getBuses().size() : 0)")
    BusOperatorResponse toResponse(BusOperator operator);

    List<BusOperatorResponse> toResponseList(List<BusOperator> operators);
}