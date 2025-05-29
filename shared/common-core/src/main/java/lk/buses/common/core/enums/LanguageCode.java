package lk.buses.common.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguageCode {
    SI("Sinhala", "සිංහල"),
    TA("Tamil", "தமிழ்"),
    EN("English", "English");

    private final String displayName;
    private final String nativeName;
}