package util;

import exception.InvalidPhoneException;
import model.FeaturePhone;
import model.Phone;
import model.Smartphone;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isValidPhoneId(String id) {
        return id != null && id.matches("[A-Za-z0-9_-]{2,20}");
    }

    public static void validatePhone(Phone phone) throws InvalidPhoneException {
        if (phone == null) throw new InvalidPhoneException("Điện thoại không được để trống.");
        if (isBlank(phone.getId())) throw new InvalidPhoneException("Mã điện thoại không được để trống.");
        if (!isValidPhoneId(phone.getId())) throw new InvalidPhoneException(
                "Mã điện thoại phải dài 2-20 ký tự và chỉ gồm chữ, số, gạch ngang hoặc gạch dưới.");
        if (isBlank(phone.getName())) throw new InvalidPhoneException("Tên điện thoại không được để trống.");
        if (isBlank(phone.getBrand())) throw new InvalidPhoneException("Hãng sản xuất không được để trống.");
        if (isBlank(phone.getOperatingSystem())) throw new InvalidPhoneException("Hệ điều hành không được để trống.");
        if (isBlank(phone.getColor())) throw new InvalidPhoneException("Màu sắc không được để trống.");
        if (phone.getPrice() <= 0) throw new InvalidPhoneException("Giá bán phải lớn hơn 0.");
        if (phone.getQuantity() < 0) throw new InvalidPhoneException("Số lượng không được âm.");
        if (phone.getStorageGB() <= 0) throw new InvalidPhoneException("Bộ nhớ phải lớn hơn 0.");
        if (phone.getRamGB() <= 0) throw new InvalidPhoneException("RAM phải lớn hơn 0.");
        if (phone.getWarrantyMonths() < 0) throw new InvalidPhoneException("Bảo hành không được âm.");
        if (phone instanceof Smartphone && ((Smartphone) phone).getCameraMegapixels() <= 0)
            throw new InvalidPhoneException("Camera phải lớn hơn 0.");
        if (phone instanceof FeaturePhone && ((FeaturePhone) phone).getBatteryCapacityMah() <= 0)
            throw new InvalidPhoneException("Dung lượng pin phải lớn hơn 0.");
    }
}
