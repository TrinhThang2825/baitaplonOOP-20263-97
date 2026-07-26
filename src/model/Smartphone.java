package model;

public class Smartphone extends Phone {
    private static final long serialVersionUID = 1L;
    private boolean supports5G;
    private int cameraMegapixels;

    public Smartphone() {
    }

    public Smartphone(String id, String name, String brand, String operatingSystem,
            double price, int quantity, String color, int storageGB, int ramGB,
            int warrantyMonths, boolean supports5G, int cameraMegapixels) {
        super(id, name, brand, operatingSystem, price, quantity, color, storageGB,
                ramGB, warrantyMonths);
        this.supports5G = supports5G;
        this.cameraMegapixels = cameraMegapixels;
    }

    public boolean isSupports5G() { return supports5G; }
    public void setSupports5G(boolean supports5G) { this.supports5G = supports5G; }
    public int getCameraMegapixels() { return cameraMegapixels; }
    public void setCameraMegapixels(int cameraMegapixels) { this.cameraMegapixels = cameraMegapixels; }
    @Override public double calculateTax() { return getPrice() * 0.10; }
    @Override public String getPhoneType() { return "Điện thoại thông minh"; }
    @Override public String getSpecialInformation() {
        return "5G: " + (supports5G ? "Có" : "Không") + ", Camera: " + cameraMegapixels + " MP";
    }
}
