package main;

import controller.PhoneController;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import repository.FilePhoneRepository;
import repository.PhoneRepository;
import service.PhoneService;
import view.PhoneStoreFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    PhoneRepository repository = new FilePhoneRepository();
                    PhoneService service = new PhoneService(repository);
                    PhoneStoreFrame frame = new PhoneStoreFrame();
                    new PhoneController(service, frame);
                    frame.setVisible(true);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null,
                            "Không thể khởi động chương trình: " + exception.getMessage(),
                            "Lỗi khởi động", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
