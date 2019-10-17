package arkpas.userdatabase.service;

import arkpas.userdatabase.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<User> getPaginatedUsers (Pageable pageable);
    Page<User> getPaginatedUsersBySurname (String surname, Pageable pageable);
    User saveUser (User user);
    List<User> saveUsers (List<User> users);
    void removeUser (long id);
    void removeAll ();
    Long getUserCount ();
    User getOldestUserWithPhoneNumber ();
}
