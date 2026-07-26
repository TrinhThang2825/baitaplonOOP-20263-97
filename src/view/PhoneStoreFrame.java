package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import model.FeaturePhone;
import model.Phone;
import model.Smartphone;
import util.CurrencyUtil;

/**
 * Giao diện chính gồm ba tab. Lớp chỉ tạo và hiển thị control;
 * toàn bộ xử lý nghiệp vụ nằm trong PhoneController và PhoneService.
 */
public class PhoneStoreFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final Color NAVY = new Color(15, 23, 42);
    private static final Color PRIMARY = new Color(37, 99, 235);
    private static final Color SUCCESS = new Color(5, 150, 105);
    private static final Color DANGER = new Color(220, 38, 38);
    private static final Color BACKGROUND = new Color(241, 245, 249);
    private static final Color BORDER = new Color(203, 213, 225);
    private static final Color TEXT = new Color(30, 41, 59);

    private final JTabbedPane tabs = new JTabbedPane();
    private final JComboBox<String> typeBox = box(
            new String[] {"Điện thoại thông minh", "Điện thoại phổ thông"}, false);
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JComboBox<String> brandBox = box(
            new String[] {"Apple", "Samsung", "Xiaomi", "Oppo", "Vivo", "Nokia", "Realme", "Huawei"}, true);
    private final JComboBox<String> osBox = box(
            new String[] {"iOS", "Android", "HarmonyOS", "Không có", "Hệ điều hành khác"}, true);
    private final JTextField priceField = new JTextField();
    private final JTextField quantityField = new JTextField();
    private final JTextField colorField = new JTextField();
    private final JTextField storageField = new JTextField();
    private final JTextField ramField = new JTextField();
    private final JTextField warrantyField = new JTextField();
    private final JCheckBox supports5GBox = new JCheckBox("Có hỗ trợ 5G");
    private final JTextField cameraField = new JTextField();
    private final JCheckBox physicalKeyboardBox = new JCheckBox("Có bàn phím vật lý");
    private final JTextField batteryField = new JTextField();
    private final CardLayout specialLayout = new CardLayout();
    private final JPanel specialPanel = new JPanel(specialLayout);
    private final JButton addButton = new JButton("Thêm");
    private final JButton updateButton = new JButton("Cập nhật");
    private final JButton deleteButton = new JButton("Xóa");
    private final JButton clearButton = new JButton("Làm mới biểu mẫu");
    private final JButton saveButton = new JButton("Lưu dữ liệu");
    private final JButton exitButton = new JButton("Thoát");
    private final JTable manageTable = createPhoneTable();

    private final JTextField keywordField = new JTextField(15);
    private final JComboBox<String> filterTypeBox = box(
            new String[] {"Tất cả", "Điện thoại thông minh", "Điện thoại phổ thông"}, false);
    private final JComboBox<String> filterBrandBox = box(
            new String[] {"Tất cả", "Apple", "Samsung", "Xiaomi", "Oppo", "Vivo",
                    "Nokia", "Realme", "Huawei", "Khác"}, false);
    private final JComboBox<String> filterOsBox = box(
            new String[] {"Tất cả", "iOS", "Android", "HarmonyOS", "Hệ điều hành khác", "Không có"}, false);
    private final JComboBox<String> stockBox = box(
            new String[] {"Tất cả", "Còn hàng", "Hết hàng", "Sắp hết hàng"}, false);
    private final JTextField minPriceField = new JTextField(9);
    private final JTextField maxPriceField = new JTextField(9);
    private final JComboBox<String> sortBox = box(
            new String[] {"Tên A-Z", "Tên Z-A", "Giá tăng dần", "Giá giảm dần",
                    "Số lượng tăng dần", "Số lượng giảm dần"}, false);
    private final JButton searchButton = new JButton("Tìm kiếm");
    private final JButton filterButton = new JButton("Áp dụng bộ lọc");
    private final JButton sortButton = new JButton("Sắp xếp");
    private final JButton resetFilterButton = new JButton("Đặt lại");
    private final JButton showAllButton = new JButton("Hiển thị tất cả");
    private final JTable filterTable = createPhoneTable();

    private final JLabel totalModelsLabel = valueLabel();
    private final JLabel totalQuantityLabel = valueLabel();
    private final JLabel inventoryValueLabel = valueLabel();
    private final JLabel inStockLabel = valueLabel();
    private final JLabel outStockLabel = valueLabel();
    private final JLabel lowStockLabel = valueLabel();
    private final JLabel expensiveLabel = valueLabel();
    private final JLabel highQuantityLabel = valueLabel();
    private final JLabel commonBrandLabel = valueLabel();
    private final JButton refreshStatisticsButton = new JButton("Làm mới thống kê");

    private final JMenuItem menuSave = new JMenuItem("Lưu dữ liệu");
    private final JMenuItem menuReload = new JMenuItem("Tải lại dữ liệu");
    private final JMenuItem menuAdd = new JMenuItem("Thêm điện thoại");
    private final JMenuItem menuClear = new JMenuItem("Làm mới biểu mẫu");
    private final JMenuItem menuShowAll = new JMenuItem("Hiển thị tất cả");

    public PhoneStoreFrame() {
        setTitle("QUẢN LÝ CỬA HÀNG BÁN ĐIỆN THOẠI");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1300, 750);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        tabs.addTab("Quản lý điện thoại", createManageTab());
        tabs.addTab("Tìm kiếm và lọc", createFilterTab());
        tabs.addTab("Thống kê", createStatisticsTab());
        add(tabs, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
        setJMenuBar(createMenuBar());
        typeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) { showSpecialPanel(); }
        });
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent event) { confirmExit(); }
        });
        showSpecialPanel();
        applyTheme(getContentPane());
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setName("header");
        panel.setBackground(NAVY);
        panel.setBorder(BorderFactory.createEmptyBorder(13, 22, 13, 22));
        JLabel title = new JLabel("PHONE STORE  •  QUẢN LÝ CỬA HÀNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("Quản lý sản phẩm, tồn kho và báo cáo tập trung");
        subtitle.setForeground(new Color(148, 163, 184));
        panel.add(title, BorderLayout.CENTER);
        panel.add(subtitle, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setName("footer");
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        JLabel status = new JLabel("●  Hệ thống sẵn sàng");
        status.setForeground(SUCCESS);
        panel.add(status, BorderLayout.WEST);
        panel.add(new JLabel("Object Serialization  |  Phiên bản 1.0"), BorderLayout.EAST);
        return panel;
    }

    private JPanel createManageTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createFormPanel(), tableScroll(manageTable, "Danh sách điện thoại"));
        split.setDividerLocation(355);
        split.setResizeWeight(0);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder("Thông tin điện thoại"));
        JPanel fields = new JPanel(new GridBagLayout());
        int row = 0;
        addField(fields, row++, "Loại điện thoại:", typeBox);
        addField(fields, row++, "Mã điện thoại:", idField);
        addField(fields, row++, "Tên điện thoại:", nameField);
        addField(fields, row++, "Hãng sản xuất:", brandBox);
        addField(fields, row++, "Hệ điều hành:", osBox);
        addField(fields, row++, "Giá bán:", priceField);
        addField(fields, row++, "Số lượng:", quantityField);
        addField(fields, row++, "Màu sắc:", colorField);
        addField(fields, row++, "Bộ nhớ (GB):", storageField);
        addField(fields, row++, "RAM (GB):", ramField);
        addField(fields, row++, "Bảo hành (tháng):", warrantyField);
        specialPanel.add(smartphonePanel(), "SMARTPHONE");
        specialPanel.add(featurePhonePanel(), "FEATURE");
        GridBagConstraints special = constraints(0, row);
        special.gridwidth = 2;
        special.fill = GridBagConstraints.HORIZONTAL;
        fields.add(specialPanel, special);
        wrapper.add(new JScrollPane(fields), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new GridLayout(3, 2, 5, 5));
        buttons.add(addButton); buttons.add(updateButton);
        buttons.add(deleteButton); buttons.add(clearButton);
        buttons.add(saveButton); buttons.add(exitButton);
        wrapper.add(buttons, BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel smartphonePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin Smartphone"));
        panel.add(supports5GBox); panel.add(new JLabel(""));
        panel.add(new JLabel("Camera (MP):")); panel.add(cameraField);
        return panel;
    }

    private JPanel featurePhonePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin điện thoại phổ thông"));
        panel.add(physicalKeyboardBox); panel.add(new JLabel(""));
        panel.add(new JLabel("Dung lượng pin (mAh):")); panel.add(batteryField);
        return panel;
    }

    private JPanel createFilterTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel controls = new JPanel(new GridBagLayout());
        controls.setBorder(BorderFactory.createTitledBorder("Điều kiện tìm kiếm và lọc"));
        addControl(controls, 0, 0, "Từ khóa:", keywordField);
        addControl(controls, 2, 0, "Loại:", filterTypeBox);
        addControl(controls, 4, 0, "Hãng:", filterBrandBox);
        addControl(controls, 0, 1, "Hệ điều hành:", filterOsBox);
        addControl(controls, 2, 1, "Kho:", stockBox);
        addControl(controls, 4, 1, "Giá từ:", minPriceField);
        addControl(controls, 6, 1, "Giá đến:", maxPriceField);
        addControl(controls, 0, 2, "Sắp xếp:", sortBox);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(searchButton); buttons.add(filterButton); buttons.add(sortButton);
        buttons.add(resetFilterButton); buttons.add(showAllButton);
        GridBagConstraints gbc = constraints(2, 2);
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.WEST;
        controls.add(buttons, gbc);
        panel.add(controls, BorderLayout.NORTH);
        panel.add(tableScroll(filterTable, "Kết quả"), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatisticsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel cards = new JPanel(new GridLayout(3, 3, 10, 10));
        cards.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cards.add(statCard("Tổng số mẫu điện thoại", totalModelsLabel));
        cards.add(statCard("Tổng số lượng máy", totalQuantityLabel));
        cards.add(statCard("Tổng giá trị kho", inventoryValueLabel));
        cards.add(statCard("Số mẫu còn hàng", inStockLabel));
        cards.add(statCard("Số mẫu hết hàng", outStockLabel));
        cards.add(statCard("Số mẫu sắp hết", lowStockLabel));
        cards.add(statCard("Điện thoại đắt nhất", expensiveLabel));
        cards.add(statCard("Số lượng nhiều nhất", highQuantityLabel));
        cards.add(statCard("Hãng phổ biến nhất", commonBrandLabel));
        panel.add(cards, BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        bottom.add(refreshStatisticsButton);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel statCard(String title, JLabel value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 1, 1, 1, PRIMARY),
                BorderFactory.createTitledBorder(title)));
        panel.add(value, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane tableScroll(JTable table, String title) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(title));
        return scroll;
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Tệp");
        JMenuItem exit = new JMenuItem("Thoát");
        file.add(menuSave); file.add(menuReload); file.addSeparator(); file.add(exit);
        JMenu manage = new JMenu("Quản lý");
        manage.add(menuAdd); manage.add(menuClear); manage.add(menuShowAll);
        JMenu help = new JMenu("Trợ giúp");
        JMenuItem guide = new JMenuItem("Hướng dẫn sử dụng");
        JMenuItem about = new JMenuItem("Giới thiệu");
        help.add(guide); help.add(about);
        bar.add(file); bar.add(manage); bar.add(help);
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) { confirmExit(); }
        });
        guide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(PhoneStoreFrame.this,
                        "1. Nhập thông tin rồi nhấn Thêm.\n"
                        + "2. Chọn một dòng để cập nhật hoặc xóa.\n"
                        + "3. Dùng tab Tìm kiếm và lọc để tra cứu.\n"
                        + "4. Dữ liệu tự lưu sau mỗi thay đổi.",
                        "Hướng dẫn", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(PhoneStoreFrame.this,
                        "QUẢN LÝ CỬA HÀNG BÁN ĐIỆN THOẠI\n"
                        + "Môn: Lập trình hướng đối tượng\n"
                        + "Trịnh Minh Đức - B23DCCN192\n"
                        + "Trịnh Xuân Thắng - B23DCCN757\nPhiên bản 1.0",
                        "Giới thiệu", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return bar;
    }

    private void addField(JPanel panel, int row, String text, Component component) {
        addControl(panel, 0, row, text, component);
    }

    private void addControl(JPanel panel, int x, int y, String text, Component component) {
        GridBagConstraints label = constraints(x, y);
        label.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(text), label);
        GridBagConstraints field = constraints(x + 1, y);
        field.weightx = 1;
        field.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, field);
    }

    private GridBagConstraints constraints(int x, int y) {
        GridBagConstraints value = new GridBagConstraints();
        value.gridx = x; value.gridy = y;
        value.insets = new Insets(3, 4, 3, 4);
        return value;
    }

    private static JComboBox<String> box(String[] values, boolean editable) {
        JComboBox<String> result = new JComboBox<String>(values);
        result.setEditable(editable);
        return result;
    }

    private static JLabel valueLabel() {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return label;
    }

    private static JTable createPhoneTable() {
        String[] columns = {"STT", "Mã", "Tên điện thoại", "Loại", "Hãng",
                "Hệ điều hành", "Giá bán", "Số lượng", "Màu sắc", "Bộ nhớ",
                "RAM", "Bảo hành", "Thông tin riêng", "Thuế", "Giá sau thuế", "Giá trị kho"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] widths = {45, 90, 190, 155, 90, 110, 115, 75, 100, 75, 60,
                90, 220, 110, 125, 125};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        return table;
    }

    private void applyTheme(Component component) {
        if (component instanceof JPanel
                && ("header".equals(component.getName()) || "footer".equals(component.getName()))) return;
        component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (component instanceof JPanel) component.setBackground(BACKGROUND);
        if (component instanceof JTextField) {
            JTextField field = (JTextField) component;
            field.setBackground(Color.WHITE);
            field.setForeground(TEXT);
            field.setBorder(new CompoundBorder(BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        }
        if (component instanceof JButton) styleButton((JButton) component);
        if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setRowHeight(30);
            table.setGridColor(new Color(226, 232, 240));
            table.setShowVerticalLines(false);
            table.setSelectionBackground(new Color(219, 234, 254));
            table.setSelectionForeground(NAVY);
            table.getTableHeader().setBackground(NAVY);
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setPreferredSize(new Dimension(0, 34));
            table.getTableHeader().setReorderingAllowed(false);
        }
        if (component instanceof Container) {
            Component[] children = ((Container) component).getComponents();
            for (int i = 0; i < children.length; i++) applyTheme(children[i]);
        }
    }

    private void styleButton(JButton button) {
        Color color = PRIMARY;
        if ("Xóa".equals(button.getText()) || "Thoát".equals(button.getText())) color = DANGER;
        else if ("Thêm".equals(button.getText()) || "Lưu dữ liệu".equals(button.getText())) color = SUCCESS;
        else if (button.getText().indexOf("Làm mới") >= 0 || "Đặt lại".equals(button.getText()))
            color = new Color(100, 116, 139);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(BorderFactory.createLineBorder(color.darker()),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
    }

    public void displayPhones(JTable table, List<Phone> phones) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        int index = 1;
        for (Phone phone : phones) {
            model.addRow(new Object[] {index++, phone.getId(), phone.getName(),
                    phone.getPhoneType(), phone.getBrand(), phone.getOperatingSystem(),
                    CurrencyUtil.formatVND(phone.getPrice()), phone.getQuantity(),
                    phone.getColor(), phone.getStorageGB() + " GB", phone.getRamGB() + " GB",
                    phone.getWarrantyMonths() + " tháng", phone.getSpecialInformation(),
                    CurrencyUtil.formatVND(phone.calculateTax()),
                    CurrencyUtil.formatVND(phone.calculatePriceAfterTax()),
                    CurrencyUtil.formatVND(phone.calculateInventoryValue())});
        }
    }

    public void fillForm(Phone phone) {
        typeBox.setSelectedItem(phone.getPhoneType());
        idField.setText(phone.getId());
        nameField.setText(phone.getName());
        brandBox.setSelectedItem(phone.getBrand());
        osBox.setSelectedItem(phone.getOperatingSystem());
        priceField.setText(String.valueOf((long) phone.getPrice()));
        quantityField.setText(String.valueOf(phone.getQuantity()));
        colorField.setText(phone.getColor());
        storageField.setText(String.valueOf(phone.getStorageGB()));
        ramField.setText(String.valueOf(phone.getRamGB()));
        warrantyField.setText(String.valueOf(phone.getWarrantyMonths()));
        if (phone instanceof Smartphone) {
            Smartphone value = (Smartphone) phone;
            supports5GBox.setSelected(value.isSupports5G());
            cameraField.setText(String.valueOf(value.getCameraMegapixels()));
        } else {
            FeaturePhone value = (FeaturePhone) phone;
            physicalKeyboardBox.setSelected(value.isHasPhysicalKeyboard());
            batteryField.setText(String.valueOf(value.getBatteryCapacityMah()));
        }
        idField.setEditable(false);
    }

    public void clearForm() {
        JTextField[] fields = {idField, nameField, priceField, quantityField,
                colorField, storageField, ramField, warrantyField, cameraField, batteryField};
        for (int i = 0; i < fields.length; i++) fields[i].setText("");
        supports5GBox.setSelected(false);
        physicalKeyboardBox.setSelected(false);
        idField.setEditable(true);
        manageTable.clearSelection();
        idField.requestFocusInWindow();
    }

    public void resetFilters() {
        keywordField.setText("");
        filterTypeBox.setSelectedIndex(0); filterBrandBox.setSelectedIndex(0);
        filterOsBox.setSelectedIndex(0); stockBox.setSelectedIndex(0);
        minPriceField.setText(""); maxPriceField.setText("");
        sortBox.setSelectedIndex(0);
    }

    private void showSpecialPanel() {
        specialLayout.show(specialPanel, isSmartphoneSelected() ? "SMARTPHONE" : "FEATURE");
    }

    public boolean isSmartphoneSelected() {
        return "Điện thoại thông minh".equals(typeBox.getSelectedItem());
    }

    public String getSelectedManageId() {
        int row = manageTable.getSelectedRow();
        return row < 0 ? null : String.valueOf(manageTable.getValueAt(row, 1));
    }

    public void confirmExit() {
        if (JOptionPane.showConfirmDialog(this, "Bạn có muốn thoát chương trình không?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) dispose();
    }

    public String value(JTextField field) { return field.getText().trim(); }
    public String selected(JComboBox<String> comboBox) {
        Object item = comboBox.isEditable()
                ? comboBox.getEditor().getItem() : comboBox.getSelectedItem();
        return item == null ? "" : item.toString().trim();
    }

    public JTabbedPane getTabs() { return tabs; }
    public JTextField getIdField() { return idField; }
    public JTextField getNameField() { return nameField; }
    public JComboBox<String> getBrandBox() { return brandBox; }
    public JComboBox<String> getOsBox() { return osBox; }
    public JTextField getPriceField() { return priceField; }
    public JTextField getQuantityField() { return quantityField; }
    public JTextField getColorField() { return colorField; }
    public JTextField getStorageField() { return storageField; }
    public JTextField getRamField() { return ramField; }
    public JTextField getWarrantyField() { return warrantyField; }
    public JCheckBox getSupports5GBox() { return supports5GBox; }
    public JTextField getCameraField() { return cameraField; }
    public JCheckBox getPhysicalKeyboardBox() { return physicalKeyboardBox; }
    public JTextField getBatteryField() { return batteryField; }
    public JButton getAddButton() { return addButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getClearButton() { return clearButton; }
    public JButton getSaveButton() { return saveButton; }
    public JButton getExitButton() { return exitButton; }
    public JTable getManageTable() { return manageTable; }
    public JTextField getKeywordField() { return keywordField; }
    public JComboBox<String> getFilterTypeBox() { return filterTypeBox; }
    public JComboBox<String> getFilterBrandBox() { return filterBrandBox; }
    public JComboBox<String> getFilterOsBox() { return filterOsBox; }
    public JComboBox<String> getStockBox() { return stockBox; }
    public JTextField getMinPriceField() { return minPriceField; }
    public JTextField getMaxPriceField() { return maxPriceField; }
    public JComboBox<String> getSortBox() { return sortBox; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getFilterButton() { return filterButton; }
    public JButton getSortButton() { return sortButton; }
    public JButton getResetFilterButton() { return resetFilterButton; }
    public JButton getShowAllButton() { return showAllButton; }
    public JTable getFilterTable() { return filterTable; }
    public JLabel getTotalModelsLabel() { return totalModelsLabel; }
    public JLabel getTotalQuantityLabel() { return totalQuantityLabel; }
    public JLabel getInventoryValueLabel() { return inventoryValueLabel; }
    public JLabel getInStockLabel() { return inStockLabel; }
    public JLabel getOutStockLabel() { return outStockLabel; }
    public JLabel getLowStockLabel() { return lowStockLabel; }
    public JLabel getExpensiveLabel() { return expensiveLabel; }
    public JLabel getHighQuantityLabel() { return highQuantityLabel; }
    public JLabel getCommonBrandLabel() { return commonBrandLabel; }
    public JButton getRefreshStatisticsButton() { return refreshStatisticsButton; }
    public JMenuItem getMenuSave() { return menuSave; }
    public JMenuItem getMenuReload() { return menuReload; }
    public JMenuItem getMenuAdd() { return menuAdd; }
    public JMenuItem getMenuClear() { return menuClear; }
    public JMenuItem getMenuShowAll() { return menuShowAll; }
}
