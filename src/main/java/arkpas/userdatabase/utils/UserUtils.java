package arkpas.userdatabase.utils;

import arkpas.userdatabase.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserUtils {
    private static final Logger logger = Logger.getLogger(UserUtils.class.getName());
    private static final DateTimeFormatter DATE_FORMAT_LONG = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter DATE_FORMAT_SHORT = DateTimeFormatter.ofPattern("yyyy.M.d");

    public static List<User> createUsersFromFile (MultipartFile file) {
        List<User> users = new ArrayList<>();

        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                users.addAll(UserUtils.createUsersFromInputStream(inputStream));
            } catch (IOException e) {
                logger.log(Level.INFO, "Error creating input stream from file", e);
            }
        }
        return users;

    }

    public static List<User> createUsersFromInputStream (InputStream inputStream) {

        List<User> users = new ArrayList<>();
        String nextLine;
        if (inputStream != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((nextLine = bufferedReader.readLine()) != null) {
                    User user = UserUtils.fillUserWithData(UserUtils.splitData(nextLine));
                    if (user != null)
                        users.add(user);
                }
            } catch (IOException e) {
                logger.log(Level.INFO, "Error reading line from file", e);
            }
        }
        else {
            logger.log(Level.INFO, "Input stream was null");
        }
        return users;
    }

    public static List<String> splitData (String data) {
        if (data == null)
            return new ArrayList<>();

        data = data.replaceAll("\\s", "");
        String[] splittedData = data.split(";");

        return Arrays.stream(splittedData).filter(s -> s.length() > 0).collect(Collectors.toList());
    }

    public static User fillUserWithData (List<String> dataList) {

        //this method will intentionally ignore any excessive data, it only checks if minimum data is provided
        User user = new User();
        if (dataList.size() >= 3) {
            user.setName(UserUtils.capitalize(dataList.get(0)));
            user.setSurname(UserUtils.capitalize(dataList.get(1)));
            try {
                user.setBirthDate(LocalDate.parse(dataList.get(2), DATE_FORMAT_LONG));
            }
            catch (DateTimeParseException longDateFormatException) {
                try {
                    user.setBirthDate(LocalDate.parse(dataList.get(2), DATE_FORMAT_SHORT));
                }
                catch (DateTimeParseException shortDateFormatException) {
                    logger.log(Level.INFO, "Wrong date format: " + dataList.get(2), shortDateFormatException);
                    return null;
                }
            }
            if (dataList.size() >= 4) {
                Pattern pattern = Pattern.compile("[0-9]{9}");
                String phoneNumberAsString = dataList.get(3);
                if (pattern.matcher(phoneNumberAsString).matches())
                    user.setPhoneNumber(Integer.parseInt(phoneNumberAsString));
            }
            return user;
        }
        else {
            logger.log(Level.INFO, "Insufficient amount of data to create user: " + dataList.toString());
            return null;
        }

    }

    public static String capitalize (String text) {
        String result = "";
        if (text == null)
            return null;
        if (text.length() >= 1)
            result = text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
        return result;
    }
}
