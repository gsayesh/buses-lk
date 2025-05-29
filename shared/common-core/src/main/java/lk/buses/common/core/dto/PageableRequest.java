package lk.buses.common.core.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageableRequest {
    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 20;

    private String sortBy = "id";
    private String sortDirection = "ASC";
}