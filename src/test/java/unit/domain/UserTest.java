package unit.domain;

import arkpas.userdatabase.domain.User;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private User user = new User();

    @Test
    public void calculateAgeShouldSetAgeToOneWhenUsersBirthdayIsToday () {
        LocalDate birthday = LocalDate.now().minusYears(1); //user is one year old and has birthday today
        user.setBirthDate(birthday);
        user.calculateAge(LocalDate.now());
        assertEquals(1, user.getAge());
    }

    @Test
    public void calculateAgeShouldSetAgeToOneWhenUsersBirthdayWasYesterday () {
        LocalDate birthday = LocalDate.now().minusYears(1).minusDays(1); //user had birthday yesterday
        user.setBirthDate(birthday);
        user.calculateAge(LocalDate.now());
        assertEquals(1, user.getAge());
    }

    @Test
    public void calculateAgeShouldSetAgeToZeroWhenUsersBirthdayIsAfterToday () {
        LocalDate birthday = LocalDate.now().minusYears(1).plusDays(1); //user have birthday tommorow
        user.setBirthDate(birthday);
        user.calculateAge(LocalDate.now());
        assertEquals(0, user.getAge());
    }

    @Test
    public void calculateAgeShouldSetAgeToZeroWhenUserHasNoBirthdayDate () {
        user.calculateAge(LocalDate.now());
        user.calculateAge(LocalDate.now());
        assertEquals(0, user.getAge());
    }
}
