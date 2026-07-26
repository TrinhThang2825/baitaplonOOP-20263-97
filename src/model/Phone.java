package model;

import java.io.Serializable;

public abstract class Phone implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String brand;
    private String operatingSystem;
    private double price;
    private int quantity;
    private String color;
    private int storageGB;
    private int ramGB;
    private int warrantyMonths;

    public Phone() {
    }

    public Phone(String id, String name, String brand, String operatingSystem,
            double price, int quantity, String color, int storageGB, int ramGB,
            int warrantyMonths) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.operatingSystem = operatingSystem;
        this.price = price;
        this.quantity = quantity;
        this.color = color;
        this.storageGB = storageGB;
        this.ramGB = ramGB;
        this.warrantyMonths = warrantyMonths;
    }

    public abstract double calculateTax();
    public abstract String getPhoneType();
    public abstract String getSpecialInformation();

    public double calculateInventoryValue() {
        return price * quantity;
    }

    public double calculatePriceAfterTax() {
        return price + calculateTax();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getOperatingSystem() { return operatingSystem; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public int getStorageGB() { return storageGB; }
    public void setStorageGB(int storageGB) { this.storageGB = storageGB; }
    public int getRamGB() { return ramGB; }
    public void setRamGB(int ramGB) { this.ramGB = ramGB; }
    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) { this.warrantyMonths = warrantyMonths; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Phone)) return false;
        Phone other = (Phone) object;
        return id != null && other.id != null && id.equalsIgnoreCase(other.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return id + " - " + name + " - " + brand;
    }
}
