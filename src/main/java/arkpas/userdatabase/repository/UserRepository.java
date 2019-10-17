package arkpas.userdatabase.repository;

import arkpas.userdatabase.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
    Page<User> findBySurname(String surname, Pageable pageable);
    User findByPhoneNumber (int phoneNumber);
}
