package arkpas.userdatabase.controller;

import arkpas.userdatabase.domain.User;
import arkpas.userdatabase.service.UserService;
import arkpas.userdatabase.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class UserController {

    private UserService userService;

    protected final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/users")
    public String redirectToUsers (Model model) {
        return "redirect:/users/0";
    }

    @RequestMapping("/users/{pageNr}")
    public String getUsersPage(@PathVariable("pageNr") int pageNr, HttpSession session, Model model) {

        int numberOfResults = PageUtils.getNumberOfResultsOnPage(session);
        long userCount = userService.getUserCount();

        String sortBy = PageUtils.getSortBy(session);
        Sort.Direction sortDirection = PageUtils.getSortDirection(sortBy);
        PageRequest pageRequest = PageRequest.of(pageNr, numberOfResults, sortDirection, sortBy);

        Page<User> page = userService.getPaginatedUsers(pageRequest);
        page.forEach(user -> user.calculateAge(LocalDate.now()));

        long lastPage = page.getTotalPages() -1;
        User oldestUserWithPhoneNumber = userService.getOldestUserWithPhoneNumber();

        model.addAttribute("oldestUserWithPhoneNumber", oldestUserWithPhoneNumber);
        model.addAttribute("userCount", userCount);
        model.addAttribute("users", page.getContent());
        model.addAttribute("pageNr", pageNr);
        model.addAttribute("lastPage", lastPage);
        return "users";
    }

    @RequestMapping("/users/search")
    public String goToSearchPage (@RequestParam("searchBy") String searchBy, @RequestParam("textToSearch") String textToSearch, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("pageNr", 0);
        return "redirect:/users/search/" + searchBy + "/" + textToSearch + "/0";
    }

    @RequestMapping("/users/search/{searchBy}/{textToSearch}/{pageNr}")
    public String searchForUsers(@PathVariable("searchBy") String searchBy, @PathVariable("textToSearch") String textToSearch, @PathVariable("pageNr") int pageNr, HttpSession session, Model model) {
        int numberOfResults = PageUtils.getNumberOfResultsOnPage(session);

        PageRequest pageRequest = PageRequest.of(pageNr, numberOfResults, PageUtils.DEFAULT_SORT_DIRECTION, PageUtils.DEFAULT_SORT_BY);
        Page<User> page;

        switch (searchBy) {
            case "surname": { page = userService.getPaginatedUsersBySurname(textToSearch, pageRequest); break; }
            default: { logger.log(Level.INFO, "Incorrect searchBy parameter: " + searchBy); return "redirect:/users"; }
        }

        page.forEach(user -> user.calculateAge(LocalDate.now()));
        long searchedUsersCount = page.getTotalElements();
        long lastPage = page.getTotalPages() -1;

        model.addAttribute("searchedUsersCount", searchedUsersCount);
        model.addAttribute("users", page.getContent());
        model.addAttribute("pageNr", pageNr);
        model.addAttribute("searchBy", searchBy);
        model.addAttribute("textToSearch", textToSearch);
        model.addAttribute("lastPage", lastPage);
        return "usersSearch";
    }

    @RequestMapping(value = "/users/setSort", method = RequestMethod.POST)
    public String setSort (@RequestParam("sortBy") String sortBy, @RequestParam("pageNr") int pageNr, HttpSession session) {
        session.setAttribute("sortBy", sortBy);
        return "redirect:/users/" + pageNr;
    }

    @RequestMapping(value = "/users/{pageNr}/remove/{id}")
    public String removeUser (@PathVariable("pageNr") int pageNr, @PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/users/" + pageNr;
    }

    @RequestMapping(value = "/users/removeAll")
    public String removeAllUsers () {
        userService.removeAll();
        return "redirect:/users";
    }

    @RequestMapping(value = "/users/search/{searchBy}/{textToSearch}/{pageNr}/remove/{id}")
    public String removeUserInSearchPage (@PathVariable("searchBy") String searchBy, @PathVariable("textToSearch") String textToSearch, @PathVariable("pageNr") int pageNr, @PathVariable("id") int id) {
        userService.removeUser(id);
        return "redirect:/users/search/" + searchBy + "/" + textToSearch + "/" + pageNr;
    }

    @RequestMapping(value = "/users/setNumberOfResults", method = RequestMethod.POST)
    public String setNumberOfResultsOnPage (@RequestParam("numberOfResults") int numberOfResults, @RequestParam("pageNr") int pageNr, HttpSession session) {
        session.setAttribute("numberOfResults", numberOfResults);
        return "redirect:/users/" + pageNr;
    }




}
