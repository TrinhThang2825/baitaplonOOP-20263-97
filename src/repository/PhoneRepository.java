package repository;

import java.io.IOException;
import java.util.List;
import model.Phone;

public interface PhoneRepository {
    List<Phone> loadPhones() throws IOException;
    void savePhones(List<Phone> phones) throws IOException;
}
