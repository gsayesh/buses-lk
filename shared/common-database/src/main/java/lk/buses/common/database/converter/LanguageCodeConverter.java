package lk.buses.common.database.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lk.buses.common.core.enums.LanguageCode;

@Converter(autoApply = true)
public class LanguageCodeConverter implements AttributeConverter<LanguageCode, String> {

    @Override
    public String convertToDatabaseColumn(LanguageCode attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase();
    }

    @Override
    public LanguageCode convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return LanguageCode.valueOf(dbData.toUpperCase());
    }
}