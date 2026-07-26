package repository;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import model.Phone;

public class FilePhoneRepository implements PhoneRepository {
    private final File dataFile;

    public FilePhoneRepository() {
        this("data/phones.dat");
    }

    public FilePhoneRepository(String path) {
        dataFile = new File(path);
    }

    private void ensureDataDirectory() throws IOException {
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Không thể tạo thư mục dữ liệu: " + parent.getPath());
        }
    }

    @Override
    public List<Phone> loadPhones() throws IOException {
        ensureDataDirectory();
        if (!dataFile.exists() || dataFile.length() == 0) return new ArrayList<Phone>();
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(dataFile))) {
            Object data = input.readObject();
            if (!(data instanceof List<?>)) throw new IOException("Tệp dữ liệu không đúng định dạng.");
            ArrayList<Phone> result = new ArrayList<Phone>();
            for (Object item : (List<?>) data) {
                if (!(item instanceof Phone)) throw new IOException("Tệp chứa dữ liệu điện thoại không hợp lệ.");
                result.add((Phone) item);
            }
            return result;
        } catch (EOFException exception) {
            return new ArrayList<Phone>();
        } catch (ClassNotFoundException exception) {
            throw new IOException("Không nhận dạng được lớp dữ liệu đã lưu.", exception);
        }
    }

    @Override
    public void savePhones(List<Phone> phones) throws IOException {
        ensureDataDirectory();
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            output.writeObject(new ArrayList<Phone>(phones));
        }
    }
}
