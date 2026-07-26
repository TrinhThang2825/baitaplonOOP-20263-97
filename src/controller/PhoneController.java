package controller;

import exception.DuplicatePhoneException;
import exception.InvalidPhoneException;
import exception.PhoneNotFoundException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import model.FeaturePhone;
import model.Phone;
import model.Smartphone;
import service.PhoneService;
import util.CurrencyUtil;
import view.PhoneStoreFrame;

public class PhoneController {
    private final PhoneService service;
    private final PhoneStoreFrame view;
    private List<Phone> currentResults;

    public PhoneController(PhoneService service, PhoneStoreFrame view) {
        this.service = service;
        this.view = view;
        initController();
        refreshAll();
        if (service.getStartupWarning() != null) showError(service.getStartupWarning());
    }

    public final void initController() {
        view.getAddButton().addActionListener(action(new Runnable() {
            public void run() { handleAddPhone(); }
        }));
        view.getUpdateButton().addActionListener(action(new Runnable() {
            public void run() { handleUpdatePhone(); }
        }));
        view.getDeleteButton().addActionListener(action(new Runnable() {
            public void run() { handleDeletePhone(); }
        }));
        view.getClearButton().addActionListener(action(new Runnable() {
            public void run() { handleReset(); }
        }));
        view.getSaveButton().addActionListener(action(new Runnable() {
            public void run() { handleSave(); }
        }));
        view.getExitButton().addActionListener(action(new Runnable() {
            public void run() { view.confirmExit(); }
        }));
        view.getSearchButton().addActionListener(action(new Runnable() {
            public void run() { handleSearch(); }
        }));
        view.getFilterButton().addActionListener(action(new Runnable() {
            public void run() { handleFilter(); }
        }));
        view.getSortButton().addActionListener(action(new Runnable() {
            public void run() { handleSort(); }
        }));
        view.getResetFilterButton().addActionListener(action(new Runnable() {
            public void run() { view.resetFilters(); refreshFilterTable(service.getAllPhones()); }
        }));
        view.getShowAllButton().addActionListener(action(new Runnable() {
            public void run() { view.resetFilters(); refreshFilterTable(service.getAllPhones()); }
        }));
        view.getRefreshStatisticsButton().addActionListener(action(new Runnable() {
            public void run() { refreshStatistics(); }
        }));
        view.getMenuSave().addActionListener(action(new Runnable() {
            public void run() { handleSave(); }
        }));
        view.getMenuReload().addActionListener(action(new Runnable() {
            public void run() { handleReload(); }
        }));
        view.getMenuAdd().addActionListener(action(new Runnable() {
            public void run() {
                view.getTabs().setSelectedIndex(0);
                view.clearForm();
            }
        }));
        view.getMenuClear().addActionListener(action(new Runnable() {
            public void run() { view.getTabs().setSelectedIndex(0); handleReset(); }
        }));
        view.getMenuShowAll().addActionListener(action(new Runnable() {
            public void run() {
                view.getTabs().setSelectedIndex(1);
                view.resetFilters();
                refreshFilterTable(service.getAllPhones());
            }
        }));
        view.getManageTable().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent event) { handleTableSelection(); }
        });
        view.getKeywordField().addActionListener(action(new Runnable() {
            public void run() { handleSearch(); }
        }));
    }

    private ActionListener action(final Runnable runnable) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) { runnable.run(); }
        };
    }

    public void handleAddPhone() {
        try {
            service.addPhone(buildPhoneFromForm());
            showSuccess("Thêm điện thoại thành công.");
            view.clearForm();
            refreshAll();
        } catch (InvalidPhoneException exception) {
            showError(exception.getMessage());
        } catch (DuplicatePhoneException exception) {
            showError(exception.getMessage());
        } catch (IOException exception) {
            showError("Không thể lưu dữ liệu: " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    public void handleUpdatePhone() {
        if (view.getSelectedManageId() == null) {
            showError("Vui lòng chọn một điện thoại trong bảng.");
            return;
        }
        try {
            service.updatePhone(buildPhoneFromForm());
            showSuccess("Cập nhật điện thoại thành công.");
            view.clearForm();
            refreshAll();
        } catch (InvalidPhoneException exception) {
            showError(exception.getMessage());
        } catch (PhoneNotFoundException exception) {
            showError(exception.getMessage());
        } catch (IOException exception) {
            showError("Không thể lưu dữ liệu: " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    public void handleDeletePhone() {
        String id = view.getSelectedManageId();
        if (id == null) {
            showError("Vui lòng chọn một điện thoại trong bảng.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc chắn muốn xóa điện thoại này không?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;
        try {
            service.deletePhone(id);
            showSuccess("Xóa điện thoại thành công.");
            view.clearForm();
            refreshAll();
        } catch (PhoneNotFoundException exception) {
            showError(exception.getMessage());
        } catch (IOException exception) {
            showError("Không thể lưu dữ liệu: " + exception.getMessage());
        }
    }

    public void handleSearch() {
        refreshFilterTable(service.searchPhones(view.value(view.getKeywordField())));
    }

    public void handleFilter() {
        try {
            Double minimum = parseOptionalPrice(view.value(view.getMinPriceField()), "Giá từ");
            Double maximum = parseOptionalPrice(view.value(view.getMaxPriceField()), "Giá đến");
            if (minimum != null && minimum.doubleValue() < 0
                    || maximum != null && maximum.doubleValue() < 0)
                throw new IllegalArgumentException("Khoảng giá không được âm.");
            if (minimum != null && maximum != null && minimum.doubleValue() > maximum.doubleValue())
                throw new IllegalArgumentException("Giá từ không được lớn hơn giá đến.");
            List<Phone> source = service.searchPhones(view.value(view.getKeywordField()));
            currentResults = service.filterPhones(source,
                    view.selected(view.getFilterTypeBox()),
                    view.selected(view.getFilterBrandBox()),
                    view.selected(view.getFilterOsBox()),
                    view.selected(view.getStockBox()), minimum, maximum);
            view.displayPhones(view.getFilterTable(), currentResults);
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    public void handleSort() {
        if (currentResults == null) currentResults = service.getAllPhones();
        currentResults = service.sortPhones(currentResults, view.selected(view.getSortBox()));
        view.displayPhones(view.getFilterTable(), currentResults);
    }

    public void handleReset() { view.clearForm(); }

    public void handleTableSelection() {
        String id = view.getSelectedManageId();
        Phone phone = service.findById(id);
        if (phone != null) view.fillForm(phone);
    }

    public void refreshPhoneTable() {
        view.displayPhones(view.getManageTable(), service.getAllPhones());
    }

    private void refreshFilterTable(List<Phone> phones) {
        currentResults = phones;
        view.displayPhones(view.getFilterTable(), phones);
    }

    public void refreshStatistics() {
        view.getTotalModelsLabel().setText(String.valueOf(service.getTotalPhoneModels()));
        view.getTotalQuantityLabel().setText(String.valueOf(service.getTotalQuantity()));
        view.getInventoryValueLabel().setText(CurrencyUtil.formatVND(service.getTotalInventoryValue()));
        view.getInStockLabel().setText(String.valueOf(service.countInStockPhones()));
        view.getOutStockLabel().setText(String.valueOf(service.countOutOfStockPhones()));
        view.getLowStockLabel().setText(String.valueOf(service.countLowStockPhones()));
        Phone expensive = service.findMostExpensivePhone();
        view.getExpensiveLabel().setText(expensive == null ? "Chưa có dữ liệu"
                : expensive.getName() + " - " + CurrencyUtil.formatVND(expensive.getPrice()));
        Phone highest = service.findHighestQuantityPhone();
        view.getHighQuantityLabel().setText(highest == null ? "Chưa có dữ liệu"
                : highest.getName() + " - " + highest.getQuantity() + " máy");
        String brand = service.findMostCommonBrand();
        view.getCommonBrandLabel().setText(brand == null ? "Chưa có dữ liệu" : brand);
    }

    public Phone buildPhoneFromForm() {
        String id = view.value(view.getIdField());
        String name = view.value(view.getNameField());
        String brand = view.selected(view.getBrandBox());
        String os = view.selected(view.getOsBox());
        double price = parseDouble(view.value(view.getPriceField()), "Giá bán");
        int quantity = parseInt(view.value(view.getQuantityField()), "Số lượng");
        String color = view.value(view.getColorField());
        int storage = parseInt(view.value(view.getStorageField()), "Bộ nhớ");
        int ram = parseInt(view.value(view.getRamField()), "Dung lượng RAM");
        int warranty = parseInt(view.value(view.getWarrantyField()), "Bảo hành");
        if (view.isSmartphoneSelected()) {
            int camera = parseInt(view.value(view.getCameraField()), "Camera");
            return new Smartphone(id, name, brand, os, price, quantity, color,
                    storage, ram, warranty, view.getSupports5GBox().isSelected(), camera);
        }
        int battery = parseInt(view.value(view.getBatteryField()), "Dung lượng pin");
        return new FeaturePhone(id, name, brand, os, price, quantity, color,
                storage, ram, warranty, view.getPhysicalKeyboardBox().isSelected(), battery);
    }

    private double parseDouble(String value, String field) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(field + " phải là một số hợp lệ.");
        }
    }

    private int parseInt(String value, String field) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(field + " phải là số nguyên hợp lệ.");
        }
    }

    private Double parseOptionalPrice(String value, String field) {
        if (value.length() == 0) return null;
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(field + " phải là một số hợp lệ.");
        }
    }

    private void handleSave() {
        try {
            service.saveData();
            showSuccess("Lưu dữ liệu thành công.");
        } catch (IOException exception) {
            showError("Không thể lưu dữ liệu: " + exception.getMessage());
        }
    }

    private void handleReload() {
        try {
            service.reloadData();
            view.clearForm();
            refreshAll();
            showSuccess("Tải lại dữ liệu thành công.");
        } catch (IOException exception) {
            showError("Không thể tải lại dữ liệu: " + exception.getMessage());
        }
    }

    private void refreshAll() {
        refreshPhoneTable();
        refreshFilterTable(service.getAllPhones());
        refreshStatistics();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(view, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
