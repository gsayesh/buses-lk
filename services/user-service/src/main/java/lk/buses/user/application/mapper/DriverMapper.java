package lk.buses.user.application.mapper;

import lk.buses.user.application.dto.response.DriverProfileResponse;
import lk.buses.user.domain.entity.DriverProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(target = "userId", source = "user.id")
    DriverProfileResponse toResponse(DriverProfile driverProfile);
}