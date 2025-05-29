package lk.buses.common.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Metadata {
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
}