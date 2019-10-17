package unit.utils;

import arkpas.userdatabase.domain.User;
import arkpas.userdatabase.utils.UserUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserUtils.class)
public class UserUtilsTest {

    @Test
    public void createUsersFromFileShouldReturnEmptyListIfArgumentIsNull () {
        List<User> users = UserUtils.createUsersFromFile(null);
        assertTrue(users.isEmpty());
    }

    @Test
    public void createUsersFromFileShouldReturnEmptyListIfArgumentIsEmptyFile () {
        List<User> users = UserUtils.createUsersFromFile(new MockMultipartFile("file", new byte[] {}));
        assertTrue(users.isEmpty());
    }

    @Test
    public void createUsersFromFileMethodShouldReturnTwoUsers () {
        List<User> users = new ArrayList<>();
        PowerMockito.stub(PowerMockito.method(UserUtils.class, "createUsersFromInputStream")).toReturn(users);

        User user1 = new User();
        User user2 = new User();
        users.add(user1);
        users.add(user2);

        List<User> createdUsers = UserUtils.createUsersFromFile(new MockMultipartFile("file", new byte[] {}));
        assertEquals(2, createdUsers.size());

    }

    @Test
    public void createUsersFromInputStreamShouldReturnEmptyListIfArgumentIsNull () {
        assertTrue(UserUtils.createUsersFromInputStream(null).isEmpty());
    }

    @Test
    public void createUsersFromInputStreamShouldReturnEmptyListIfArgumentContainsNoData () throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("empty.csv");
        assertTrue(UserUtils.createUsersFromInputStream(inputStream).isEmpty());
    }

    @Test
    public void createUsersFromInputStreamShouldReturnOneUser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("notempty.csv");
        assertEquals(1, UserUtils.createUsersFromInputStream(inputStream).size());
    }

    @Test
    public void createUsersFromInputStreamShouldReturnEmptyListWhenWrongDataIsProvided() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("wrongdata.csv");
        assertTrue(UserUtils.createUsersFromInputStream(inputStream).isEmpty());
    }

    @Test
    public void splitDataShouldReturnEmptyListWhenInputIsNull ()  {
        List<String> splitData = UserUtils.splitData(null);
        assertTrue(splitData.isEmpty());
    }

    @Test
    public void splitDataShouldReturnEmptyListWhenInputContainsOnlySemicolons ()  {
        List<String> splitData = UserUtils.splitData(";;;;");
        assertTrue(splitData.isEmpty());
    }

    @Test
    public void splitDataShouldReturnEmptyListWhenInputContainsOnlyWhitespaceCharacters ()  {
            List<String> splitData = UserUtils.splitData("; ;   ;   ;");
            assertTrue(splitData.isEmpty());
    }

    @Test
    public void fillUserWithDataMethodShouldReturnNullIfNotEnoughDataIsProvided () {
        List<String> data = new ArrayList<>();
        data.add("Jan");
        data.add("Testowy");
        assertNull(UserUtils.fillUserWithData(data));
    }

    @Test
    public void fillUserWithDataMethodShouldCovertDateInLongFormat () {
        List<String> data = new ArrayList<>();
        data.add("Marek");
        data.add("Testowy");
        data.add("1999.01.01");
        assertNotNull(UserUtils.fillUserWithData(data));
    }

    @Test
    public void fillUserWithDataMethodShouldCovertDateInShortFormat () {
        List<String> data = new ArrayList<>();
        data.add("Adam");
        data.add("Testowy");
        data.add("1999.2.2");
        assertNotNull(UserUtils.fillUserWithData(data));
    }

    @Test
    public void fillUserWithDataMethodShouldReturnNullIfDateFormatIsWrong () {
        List<String> data = new ArrayList<>();
        data.add("Bogdan");
        data.add("Testowy");
        data.add("1999-3-3");
        assertNull(UserUtils.fillUserWithData(data));
    }

    @Test
    public void fillUserWithDataMethodShouldAcceptNineDigitPhoneNumber () {
        int phoneNumber = 123456789;
        List<String> data = new ArrayList<>();
        data.add("Marcin");
        data.add("Testowy");
        data.add("1999.4.4");
        data.add(phoneNumber + "");

        User user = UserUtils.fillUserWithData(data);

        assertNotNull(user);
        assertEquals(phoneNumber, user.getPhoneNumber());
    }

    @Test
    public void fillUserWithDataMethodShouldNotAcceptAnyOtherFormatThanNineDigitPhoneNumber () {
        int phoneNumber1 = 1234567890;
        List<String> data1 = new ArrayList<>();
        data1.add("Arek");
        data1.add("Testowy");
        data1.add("1999.5.5");
        data1.add(phoneNumber1 + "");

        User user1 = UserUtils.fillUserWithData(data1);

        String phoneNumber2 = "122-22-11";
        List<String> data2 = new ArrayList<>();
        data2.add("Maciej");
        data2.add("Testowy");
        data2.add("1999.5.5");
        data2.add(phoneNumber2);

        User user2 = UserUtils.fillUserWithData(data2);

        assertNotNull(user1);
        assertEquals(0, user1.getPhoneNumber());

        assertNotNull(user2);
        assertEquals(0, user2.getPhoneNumber());

    }

    @Test
    public void capitalizeShouldUppercaseFirstLetterAndLowercaseOtherLetters () {
        String text = "i_WANT_tO_bE_CapITalIzed";
        String formattedText = UserUtils.capitalize(text);
        assertEquals("I_want_to_be_capitalized", formattedText);
    }

    @Test
    public void capitalizeShouldReturnNullIfArgumentIsNull () {
        String text = null;
        assertNull(UserUtils.capitalize(text));
    }

    @Test
    public void capitalizeShouldReturnEmptyStringIfArgumentIsEmpty () {
        String text = "";
        assertEquals("", UserUtils.capitalize(text));
    }



}
