package arkpas.userdatabase.controller;

import arkpas.userdatabase.domain.User;
import arkpas.userdatabase.service.UserService;
import arkpas.userdatabase.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class FileController {

    private UserService userService;

    @Autowired
    public FileController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/users/fileUpload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {

        List<User> users = UserUtils.createUsersFromFile(file);
        userService.saveUsers(users);

        return "redirect:/users";
    }
}
