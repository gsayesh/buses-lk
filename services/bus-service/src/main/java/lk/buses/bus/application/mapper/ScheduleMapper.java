package lk.buses.bus.application.mapper;

import lk.buses.bus.application.dto.request.CreateScheduleRequest;
import lk.buses.bus.application.dto.response.BusScheduleResponse;
import lk.buses.bus.domain.entity.BusSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {BusMapper.class})
public interface ScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bus", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BusSchedule toEntity(CreateScheduleRequest request);

    @Mapping(target = "routeNumber", ignore = true)
    @Mapping(target = "routeName", ignore = true)
    @Mapping(target = "operatingDays", expression = "java(mapDaysOfWeek(schedule.getDaysOfWeek()))")
    BusScheduleResponse toResponse(BusSchedule schedule);

    List<BusScheduleResponse> toResponseList(List<BusSchedule> schedules);

    default List<String> mapDaysOfWeek(List<Integer> days) {
        if (days == null) return List.of();
        return days.stream()
                .map(day -> DayOfWeek.of(day).name())
                .collect(Collectors.toList());
    }
}