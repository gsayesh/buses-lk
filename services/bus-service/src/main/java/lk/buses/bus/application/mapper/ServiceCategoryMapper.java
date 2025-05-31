package lk.buses.bus.application.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.buses.bus.application.dto.response.ServiceCategoryResponse;
import lk.buses.bus.domain.entity.ServiceCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ServiceCategoryMapper {

    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(target = "amenities", qualifiedByName = "jsonToList")
    public abstract ServiceCategoryResponse toResponse(ServiceCategory serviceCategory);

    @Named("jsonToList")
    protected List<String> jsonToList(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            // Log the error if needed
            return Collections.emptyList();
        }
    }

    @Named("listToJson")
    protected String listToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }

        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            // Log the error if needed
            return "[]";
        }
    }
}