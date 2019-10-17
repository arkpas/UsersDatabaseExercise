package arkpas.userdatabase.service;

import arkpas.userdatabase.domain.User;
import arkpas.userdatabase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> getPaginatedUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> getPaginatedUsersBySurname(String surname, Pageable pageable) {
        return userRepository.findBySurname(surname, pageable);
    }

    @Override
    public User saveUser(User user) {
        if (user.getPhoneNumber() == 0 || userRepository.findByPhoneNumber(user.getPhoneNumber()) == null)
            return userRepository.save(user);
        else {
            logger.log(Level.INFO, "Duplicate phone number: " + user);
            return null;
        }
    }

    @Override
    public List<User> saveUsers(List<User> users) {
        List<User> savedUsers = new ArrayList<>();
        users.forEach(user -> {
            User savedUser = this.saveUser(user);
            if (savedUser != null)
                savedUsers.add(savedUser);
        });
        return savedUsers;
    }

    @Override
    public void removeUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void removeAll() {
        userRepository.deleteAll();
    }

    @Override
    public Long getUserCount () {
        return userRepository.count();
    }

    @Override
    public User getOldestUserWithPhoneNumber() {
        Optional<User> optional = StreamSupport.stream(userRepository.findAll().spliterator(), false).filter(user -> user.getPhoneNumber() > 0).min(Comparator.comparing(User::getBirthDate));
        return optional.orElse(null);
    }


}
