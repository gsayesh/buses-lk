package lk.buses.common.messaging.event;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserRegisteredEvent extends BaseEvent {
    private UUID userId;
    private String mobileNumber;
    private String email;
    private String firstName;
    private String lastName;

    @Builder
    public UserRegisteredEvent(UUID userId, String mobileNumber, String email,
                               String firstName, String lastName) {
        super("USER_REGISTERED", "user-service");
        this.userId = userId;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}