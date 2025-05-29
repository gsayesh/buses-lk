package lk.buses.common.core.util;

import java.util.Random;

public class StringUtil {
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    public static String generateReference(String prefix, int length) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String sanitizeMobileNumber(String mobileNumber) {
        if (mobileNumber == null) return null;
        // Remove all non-digits
        String cleaned = mobileNumber.replaceAll("[^0-9]", "");
        // Add country code if not present
        if (cleaned.length() == 9 && cleaned.startsWith("7")) {
            cleaned = "94" + cleaned;
        } else if (cleaned.length() == 10 && cleaned.startsWith("0")) {
            cleaned = "94" + cleaned.substring(1);
        }
        return cleaned;
    }
}