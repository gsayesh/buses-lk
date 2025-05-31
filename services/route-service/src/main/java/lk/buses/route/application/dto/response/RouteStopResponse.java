package lk.buses.route.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RouteStopResponse {
    private UUID id;
    private Integer stopSequence;
    private String stopNameEn;
    private String stopNameSi;
    private String stopNameTa;
    private Double latitude;
    private Double longitude;
    private boolean isMajorStop;
}