package util;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyUtil {
    private CurrencyUtil() {
    }

    public static String formatVND(double amount) {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);
        return format.format(amount) + " ₫";
    }
}
