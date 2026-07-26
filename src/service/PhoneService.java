package service;

import exception.DuplicatePhoneException;
import exception.InvalidPhoneException;
import exception.PhoneNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.FeaturePhone;
import model.Phone;
import model.Smartphone;
import repository.PhoneRepository;
import util.ValidationUtil;

public class PhoneService {
    private ArrayList<Phone> phones;
    private final PhoneRepository repository;
    private String startupWarning;

    public PhoneService(PhoneRepository repository) {
        this.repository = repository;
        try {
            phones = new ArrayList<Phone>(repository.loadPhones());
        } catch (IOException exception) {
            phones = new ArrayList<Phone>();
            startupWarning = "Không thể đọc dữ liệu cũ: " + exception.getMessage();
        }
        if (phones.isEmpty()) {
            createSampleData();
            try {
                saveData();
            } catch (IOException exception) {
                startupWarning = "Đã tạo dữ liệu mẫu nhưng chưa thể lưu: " + exception.getMessage();
            }
        }
    }

    public String getStartupWarning() { return startupWarning; }
    public List<Phone> getAllPhones() { return new ArrayList<Phone>(phones); }

    public void addPhone(Phone phone) throws InvalidPhoneException,
            DuplicatePhoneException, IOException {
        ValidationUtil.validatePhone(phone);
        if (findById(phone.getId()) != null)
            throw new DuplicatePhoneException("Mã điện thoại " + phone.getId() + " đã tồn tại.");
        phones.add(phone);
        saveData();
    }

    public void updatePhone(Phone phone) throws InvalidPhoneException,
            PhoneNotFoundException, IOException {
        ValidationUtil.validatePhone(phone);
        int index = indexOfId(phone.getId());
        if (index < 0) throw notFound(phone.getId());
        phones.set(index, phone);
        saveData();
    }

    public void deletePhone(String id) throws PhoneNotFoundException, IOException {
        int index = indexOfId(id);
        if (index < 0) throw notFound(id);
        phones.remove(index);
        saveData();
    }

    public Phone findById(String id) {
        int index = indexOfId(id);
        return index < 0 ? null : phones.get(index);
    }

    private int indexOfId(String id) {
        if (id == null) return -1;
        for (int i = 0; i < phones.size(); i++)
            if (id.equalsIgnoreCase(phones.get(i).getId())) return i;
        return -1;
    }

    private PhoneNotFoundException notFound(String id) {
        return new PhoneNotFoundException("Không tìm thấy điện thoại có mã " + id + ".");
    }

    public List<Phone> searchPhones(String keyword) {
        String key = keyword == null ? "" : keyword.trim().toLowerCase();
        if (key.length() == 0) return getAllPhones();
        List<Phone> result = new ArrayList<Phone>();
        for (Phone phone : phones) {
            if (contains(phone.getId(), key) || contains(phone.getName(), key)
                    || contains(phone.getBrand(), key) || contains(phone.getOperatingSystem(), key)
                    || contains(phone.getColor(), key) || contains(phone.getPhoneType(), key))
                result.add(phone);
        }
        return result;
    }

    private boolean contains(String value, String key) {
        return value != null && value.toLowerCase().contains(key);
    }

    public List<Phone> filterPhones(String phoneType, String brand,
            String operatingSystem, String stockStatus, Double minPrice, Double maxPrice) {
        return filterPhones(phones, phoneType, brand, operatingSystem, stockStatus, minPrice, maxPrice);
    }

    public List<Phone> filterPhones(List<Phone> source, String phoneType, String brand,
            String operatingSystem, String stockStatus, Double minPrice, Double maxPrice) {
        List<Phone> result = new ArrayList<Phone>();
        for (Phone phone : source) {
            if (!isAll(phoneType) && !phone.getPhoneType().equalsIgnoreCase(phoneType)) continue;
            if (!matchesBrand(phone, brand)) continue;
            if (!matchesOperatingSystem(phone, operatingSystem)) continue;
            if (!matchesStock(phone, stockStatus)) continue;
            if (minPrice != null && phone.getPrice() < minPrice.doubleValue()) continue;
            if (maxPrice != null && phone.getPrice() > maxPrice.doubleValue()) continue;
            result.add(phone);
        }
        return result;
    }

    private boolean isAll(String value) {
        return value == null || value.trim().length() == 0 || "Tất cả".equalsIgnoreCase(value);
    }

    private boolean matchesBrand(Phone phone, String brand) {
        if (isAll(brand)) return true;
        if ("Khác".equalsIgnoreCase(brand)) {
            String known = "Apple,Samsung,Xiaomi,Oppo,Vivo,Nokia,Realme,Huawei";
            return !known.toLowerCase().contains(phone.getBrand().toLowerCase());
        }
        return phone.getBrand().equalsIgnoreCase(brand);
    }

    private boolean matchesOperatingSystem(Phone phone, String os) {
        if (isAll(os)) return true;
        if ("Hệ điều hành khác".equalsIgnoreCase(os)) {
            return !phone.getOperatingSystem().equalsIgnoreCase("iOS")
                    && !phone.getOperatingSystem().equalsIgnoreCase("Android")
                    && !phone.getOperatingSystem().equalsIgnoreCase("HarmonyOS")
                    && !phone.getOperatingSystem().equalsIgnoreCase("Không có");
        }
        return phone.getOperatingSystem().equalsIgnoreCase(os);
    }

    private boolean matchesStock(Phone phone, String stockStatus) {
        if (isAll(stockStatus)) return true;
        if ("Còn hàng".equalsIgnoreCase(stockStatus)) return phone.getQuantity() > 0;
        if ("Hết hàng".equalsIgnoreCase(stockStatus)) return phone.getQuantity() == 0;
        if ("Sắp hết hàng".equalsIgnoreCase(stockStatus))
            return phone.getQuantity() >= 1 && phone.getQuantity() <= 5;
        return true;
    }

    public List<Phone> sortPhones(List<Phone> source, String sortType) {
        List<Phone> result = new ArrayList<Phone>(source);
        Comparator<Phone> comparator;
        if ("Tên Z-A".equals(sortType)) {
            comparator = nameComparator(false);
        } else if ("Giá tăng dần".equals(sortType)) {
            comparator = priceComparator(true);
        } else if ("Giá giảm dần".equals(sortType)) {
            comparator = priceComparator(false);
        } else if ("Số lượng tăng dần".equals(sortType)) {
            comparator = quantityComparator(true);
        } else if ("Số lượng giảm dần".equals(sortType)) {
            comparator = quantityComparator(false);
        } else {
            comparator = nameComparator(true);
        }
        Collections.sort(result, comparator);
        return result;
    }

    private Comparator<Phone> nameComparator(final boolean ascending) {
        return new Comparator<Phone>() {
            public int compare(Phone a, Phone b) {
                int value = a.getName().compareToIgnoreCase(b.getName());
                return ascending ? value : -value;
            }
        };
    }

    private Comparator<Phone> priceComparator(final boolean ascending) {
        return new Comparator<Phone>() {
            public int compare(Phone a, Phone b) {
                int value = Double.compare(a.getPrice(), b.getPrice());
                return ascending ? value : -value;
            }
        };
    }

    private Comparator<Phone> quantityComparator(final boolean ascending) {
        return new Comparator<Phone>() {
            public int compare(Phone a, Phone b) {
                int value = a.getQuantity() < b.getQuantity() ? -1
                        : (a.getQuantity() == b.getQuantity() ? 0 : 1);
                return ascending ? value : -value;
            }
        };
    }

    public int getTotalPhoneModels() { return phones.size(); }
    public int getTotalQuantity() {
        int total = 0;
        for (Phone phone : phones) total += phone.getQuantity();
        return total;
    }
    public double getTotalInventoryValue() {
        double total = 0;
        for (Phone phone : phones) total += phone.calculateInventoryValue();
        return total;
    }
    public int countInStockPhones() {
        int count = 0;
        for (Phone phone : phones) if (phone.getQuantity() > 0) count++;
        return count;
    }
    public int countOutOfStockPhones() {
        int count = 0;
        for (Phone phone : phones) if (phone.getQuantity() == 0) count++;
        return count;
    }
    public int countLowStockPhones() {
        int count = 0;
        for (Phone phone : phones)
            if (phone.getQuantity() >= 1 && phone.getQuantity() <= 5) count++;
        return count;
    }
    public Phone findMostExpensivePhone() {
        Phone found = null;
        for (Phone phone : phones)
            if (found == null || phone.getPrice() > found.getPrice()) found = phone;
        return found;
    }
    public Phone findHighestQuantityPhone() {
        Phone found = null;
        for (Phone phone : phones)
            if (found == null || phone.getQuantity() > found.getQuantity()) found = phone;
        return found;
    }
    public Map<String, Integer> countPhonesByBrand() {
        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Phone phone : phones) {
            Integer count = result.get(phone.getBrand());
            result.put(phone.getBrand(), count == null ? 1 : count + 1);
        }
        return result;
    }
    public String findMostCommonBrand() {
        String brand = null;
        int maximum = 0;
        for (Map.Entry<String, Integer> entry : countPhonesByBrand().entrySet()) {
            if (entry.getValue() > maximum) {
                brand = entry.getKey();
                maximum = entry.getValue();
            }
        }
        return brand;
    }
    public void saveData() throws IOException { repository.savePhones(phones); }

    public void reloadData() throws IOException {
        phones = new ArrayList<Phone>(repository.loadPhones());
    }

    private void createSampleData() {
        phones.add(new Smartphone("IP15PM", "iPhone 15 Pro Max", "Apple", "iOS",
                29990000, 8, "Titan tự nhiên", 256, 8, 12, true, 48));
        phones.add(new Smartphone("SS24U", "Samsung Galaxy S24 Ultra", "Samsung", "Android",
                26990000, 5, "Xám Titan", 256, 12, 12, true, 200));
        phones.add(new Smartphone("XM14", "Xiaomi 14", "Xiaomi", "Android",
                17990000, 6, "Đen", 256, 12, 18, true, 50));
        phones.add(new Smartphone("OPRENO12", "Oppo Reno 12", "Oppo", "Android",
                12990000, 4, "Bạc", 256, 12, 12, true, 50));
        phones.add(new Smartphone("VIVO-V30", "Vivo V30", "Vivo", "Android",
                11990000, 0, "Xanh", 256, 12, 12, true, 50));
        phones.add(new Smartphone("RM12P", "Realme 12 Pro", "Realme", "Android",
                9990000, 3, "Xanh dương", 256, 8, 12, true, 50));
        phones.add(new FeaturePhone("NOKIA-105", "Nokia 105", "Nokia", "Không có",
                650000, 20, "Đen", 1, 1, 12, true, 1000));
        phones.add(new FeaturePhone("NOKIA-110", "Nokia 110", "Nokia", "Không có",
                850000, 2, "Xanh", 1, 1, 12, true, 1450));
    }
}
