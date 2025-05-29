package lk.buses.common.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceCategory {
    NORMAL("Normal", "සාමාන්‍ය", "சாதாரண", 1.0),
    SEMI_LUXURY("Semi-Luxury", "අර්ධ සුඛෝපභෝගී", "அரை ஆடம்பர", 1.5),
    AC_LUXURY("A/C Luxury", "වායු සමීකරණ සුඛෝපභෝගී", "ஏசி ஆடம்பர", 2.0),
    SUPER_LUXURY("Super Luxury", "සුපිරි සුඛෝපභෝගී", "சூப்பர் ஆடம்பர", 3.0);

    private final String displayNameEn;
    private final String displayNameSi;
    private final String displayNameTa;
    private final double fareMultiplier;
}