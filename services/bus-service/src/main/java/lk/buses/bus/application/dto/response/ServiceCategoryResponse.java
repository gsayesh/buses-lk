package lk.buses.bus.application.dto.response;

import lk.buses.common.core.enums.ServiceCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ServiceCategoryResponse {
    private UUID id;
    private ServiceCategory category;
    private Double fareMultiplier;
    private String displayNameEn;
    private String displayNameSi;
    private String displayNameTa;
    private List<String> amenities;
}
