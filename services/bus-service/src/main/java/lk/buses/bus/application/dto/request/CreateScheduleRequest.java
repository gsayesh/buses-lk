package lk.buses.bus.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class CreateScheduleRequest {
    @NotNull(message = "Route ID is required")
    private UUID routeId;

    private UUID busId;   // Optional for rotational routes

    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;

    @NotEmpty(message = "Days of week are required")
    @Size(min = 1, max = 7)
    private List<Integer> daysOfWeek;
}
