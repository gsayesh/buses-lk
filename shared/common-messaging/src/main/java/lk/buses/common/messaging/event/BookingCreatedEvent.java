package lk.buses.common.messaging.event;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingCreatedEvent extends BaseEvent {
    private UUID bookingId;
    private String bookingReference;
    private UUID userId;
    private UUID scheduleId;
    private LocalDate travelDate;
    private List<Integer> seatNumbers;
    private BigDecimal totalFare;

    @Builder
    public BookingCreatedEvent(UUID bookingId, String bookingReference, UUID userId,
                               UUID scheduleId, LocalDate travelDate,
                               List<Integer> seatNumbers, BigDecimal totalFare) {
        super("BOOKING_CREATED", "booking-service");
        this.bookingId = bookingId;
        this.bookingReference = bookingReference;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.travelDate = travelDate;
        this.seatNumbers = seatNumbers;
        this.totalFare = totalFare;
    }
}