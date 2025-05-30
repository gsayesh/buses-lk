package lk.buses.bus.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class BusScheduleResponse {
    private UUID id;
    private UUID routeId;
    private String routeNumber;
    private String routeName;
    private BusResponse bus;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private List<Integer> daysOfWeek;
    private List<String> operatingDays;
    private boolean isActive;
}
