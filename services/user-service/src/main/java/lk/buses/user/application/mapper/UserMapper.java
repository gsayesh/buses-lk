package lk.buses.user.application.mapper;

import lk.buses.user.application.dto.request.UserRegistrationRequest;
import lk.buses.user.application.dto.response.UserResponse;
import lk.buses.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "role", constant = "PASSENGER")
    @Mapping(target = "isVerified", constant = "false")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "driverProfile", ignore = true)
    User toEntity(UserRegistrationRequest request);

    UserResponse toResponse(User user);
}
