package unit.service;

import arkpas.userdatabase.domain.User;
import arkpas.userdatabase.repository.UserRepository;
import arkpas.userdatabase.service.UserService;
import arkpas.userdatabase.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void setup () {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void saveUserShouldSaveAllUsersWhoDoesNotHavePhoneNumbers () {
        doReturn(new User()).when(userRepository).save(any());
        User user1 = new User();
        user1.setName("Test");
        user1.setSurname("Test");
        user1.setBirthDate(LocalDate.now());

        User user2 = new User();
        user2.setName("AnotherTest");
        user2.setSurname("AnotherTest");
        user2.setBirthDate(LocalDate.now());

        assertNotNull(userService.saveUser(user1));
        assertNotNull(userService.saveUser(user2));

        verify(userRepository, times(2)).save(any());

    }

    @Test
    public void saveUserShouldNotSaveUserWithDuplicatePhoneNumber () {
        int phoneNumber = 111222333;
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setBirthDate(LocalDate.now());

        doReturn(user).when(userRepository).findByPhoneNumber(phoneNumber);
        doReturn(null).when(userRepository).findByPhoneNumber(not(eq(phoneNumber)));
        doReturn(new User()).when(userRepository).save(any());

        User userWithDuplicatePhone = new User();
        userWithDuplicatePhone.setPhoneNumber(phoneNumber);
        userWithDuplicatePhone.setBirthDate(LocalDate.now());
        assertNull(userService.saveUser(user));

        User userWithDifferentPhone = new User();
        userWithDifferentPhone.setPhoneNumber(phoneNumber + 1);
        userWithDifferentPhone.setBirthDate(LocalDate.now());
        assertNotNull(userService.saveUser(userWithDifferentPhone));
    }

    @Test
    public void saveUsersMethodShouldNotReturnUserIfHeWasNotSaved () {
        doReturn(new User()).when(userRepository).findByPhoneNumber(anyInt());

        User user1 = new User();
        user1.setPhoneNumber(111222333);
        User user2 = new User();
        user2.setPhoneNumber(111222333);

        List<User> usersWhichShouldNotBeSaved = new ArrayList<>();
        usersWhichShouldNotBeSaved.add(user1);
        usersWhichShouldNotBeSaved.add(user2);

        List<User> usersSaved = userService.saveUsers(usersWhichShouldNotBeSaved);

        assertTrue(usersSaved.isEmpty());
    }

    @Test
    public void saveUsersShouldReturnAllUsersThatWereSaved () {
        doReturn(new User()).when(userRepository).save(any());

        User user1 = new User();
        User user2 = new User();

        List<User> usersWhichShouldBeSaved = new ArrayList<>();
        usersWhichShouldBeSaved.add(user1);
        usersWhichShouldBeSaved.add(user2);

        List<User> savedUsers = userService.saveUsers(usersWhichShouldBeSaved);

        assertEquals(2, savedUsers.size());

    }

    @Test
    public void getOldestUserWithPhoneNumberShouldReturnNullIfListIsEmpty () {
        doReturn(new ArrayList<>()).when(userRepository).findAll();
        assertNull(userService.getOldestUserWithPhoneNumber());
    }

    @Test
    public void getOldestUserWithPhoneNumberShouldReturnNullIfNoUserHavePhoneNumber () {
        List<User> users = new ArrayList<>();
        doReturn(users).when(userRepository).findAll();

        User user1 = new User();
        user1.setName("Jan");
        user1.setSurname("Kowalski");
        user1.setBirthDate(LocalDate.now());
        users.add(user1);

        User user2 = new User();
        user2.setName("Jan");
        user2.setSurname("Niekowalski");
        user2.setBirthDate(LocalDate.now());
        users.add(user2);

        assertNull(userService.getOldestUserWithPhoneNumber());
    }

    @Test
    public void getOldestUserWithPhoneNumberShouldReturnOldestUser () {

        LocalDate oldestUserBirthday = LocalDate.of(1999, 12, 1);

        List<User> users = new ArrayList<>();
        doReturn(users).when(userRepository).findAll();

        User user1 = new User();
        user1.setName("Jan");
        user1.setSurname("Mlodszy");
        user1.setBirthDate(oldestUserBirthday.plusDays(2));
        user1.setPhoneNumber(111222333);
        users.add(user1);

        User user2 = new User();
        user2.setName("Jan");
        user2.setSurname("Sredni");
        user2.setBirthDate(oldestUserBirthday.plusDays(1));
        user2.setPhoneNumber(222333444);
        users.add(user2);

        User user3 = new User();
        user3.setName("Jan");
        user3.setSurname("Starszy");
        user3.setBirthDate(oldestUserBirthday);
        user3.setPhoneNumber(333444555);
        users.add(user3);


        assertEquals(oldestUserBirthday, userService.getOldestUserWithPhoneNumber().getBirthDate());
    }


}
