package model;

public class FeaturePhone extends Phone {
    private static final long serialVersionUID = 1L;
    private boolean hasPhysicalKeyboard;
    private int batteryCapacityMah;

    public FeaturePhone() {
    }

    public FeaturePhone(String id, String name, String brand, String operatingSystem,
            double price, int quantity, String color, int storageGB, int ramGB,
            int warrantyMonths, boolean hasPhysicalKeyboard, int batteryCapacityMah) {
        super(id, name, brand, operatingSystem, price, quantity, color, storageGB,
                ramGB, warrantyMonths);
        this.hasPhysicalKeyboard = hasPhysicalKeyboard;
        this.batteryCapacityMah = batteryCapacityMah;
    }

    public boolean isHasPhysicalKeyboard() { return hasPhysicalKeyboard; }
    public void setHasPhysicalKeyboard(boolean value) { hasPhysicalKeyboard = value; }
    public int getBatteryCapacityMah() { return batteryCapacityMah; }
    public void setBatteryCapacityMah(int value) { batteryCapacityMah = value; }
    @Override public double calculateTax() { return getPrice() * 0.05; }
    @Override public String getPhoneType() { return "Điện thoại phổ thông"; }
    @Override public String getSpecialInformation() {
        return "Bàn phím vật lý: " + (hasPhysicalKeyboard ? "Có" : "Không")
                + ", Pin: " + batteryCapacityMah + " mAh";
    }
}
