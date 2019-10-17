package integration.controller;

import arkpas.userdatabase.MainApp;
import arkpas.userdatabase.controller.UserController;
import arkpas.userdatabase.domain.User;
import arkpas.userdatabase.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MainApp.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserController userController;

    @Before
    public void setup () {
        userController = new UserController(userService);
    }

    @Test
    public void userControllerShouldRedirectToFirstPageOfUsers () throws Exception {

        mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/0"));
    }

    @Test
    public void userControllerShouldReturnUsersPage () throws Exception {
        doReturn(0L).when(userService).getUserCount();
        doReturn(Page.empty()).when(userService).getPaginatedUsers(any());
        doReturn(new User()).when(userService).getOldestUserWithPhoneNumber();

        mockMvc.perform(get("/users/0"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("oldestUserWithPhoneNumber", "userCount", "users", "pageNr", "lastPage"));
    }

    @Test
    public void userControllerShouldSetSortByValueInSession () throws Exception{
        String sortBy = "id";
        String pageNr = "1";
        HttpSession session = mockMvc.perform(post("/users/setSort").param("sortBy", sortBy).param("pageNr", pageNr))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/" + pageNr))
                .andReturn().getRequest().getSession();
        assertNotNull(session);
        assertEquals(sortBy, session.getAttribute("sortBy"));
    }

    @Test
    public void userControllerShouldRedirectToSearchPage () throws Exception {
        String searchBy = "id";
        String searchText = "2";

        mockMvc.perform(post("/users/search").param("searchBy", searchBy).param("textToSearch", searchText))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/search/" + searchBy + "/" + searchText + "/0"))
                .andExpect(flash().attributeExists("pageNr"));

    }

    @Test
    public void userControllerShouldRedirectIfSearchParameterIsWrong () throws Exception {
        String searchBy = "i_do_not_exist";
        String searchText = "text";
        String pageNr = "0";

        mockMvc.perform(get("/users/search/" + searchBy + "/" + searchText + "/" + pageNr))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users"));
    }

    @Test
    public void userControllerShouldReturnSearchResultsForSurname () throws Exception {
        String searchBy = "surname";
        String searchText = "text";
        String pageNr = "0";
        doReturn(Page.empty()).when(userService).getPaginatedUsersBySurname(anyString(), any());

        mockMvc.perform(get("/users/search/" + searchBy + "/" + searchText + "/" + pageNr))
                .andExpect(status().isOk())
                .andExpect(view().name("usersSearch"))
                .andExpect(model().attributeExists("searchedUsersCount", "users", "pageNr", "searchBy", "textToSearch", "lastPage"));
    }

    @Test
    public void userControllerShouldRemoveUserWithCertainIdAndRedirectToTheSamePageNumber () throws  Exception{
        String userId = "11";
        String pageNr = "3";
        doNothing().when(userService).removeUser(anyLong());

        mockMvc.perform(get("/users/" + pageNr + "/remove/" + userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/" + pageNr));

        verify(userService, times(1)).removeUser(Integer.parseInt(userId));
    }

    @Test
    public void userControllerShouldRemoveAllUsersAndRedirectToUsersPage () throws  Exception{
        doNothing().when(userService).removeAll();

        mockMvc.perform(get("/users/removeAll"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users"));

        verify(userService, times(1)).removeAll();
    }

    @Test
    public void userControllerShouldRemoveUserAndRedirectToTheSameSearchPage () throws  Exception{
        String userId = "22";
        String pageNr = "5";
        String searchBy = "surname";
        String textToSearch = "Kowalski";

        doNothing().when(userService).removeUser(anyLong());

        mockMvc.perform(get("/users/search/" + searchBy + "/" + textToSearch + "/" + pageNr + "/remove/" + userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/search/" + searchBy + "/" + textToSearch + "/" + pageNr));

        verify(userService, times(1)).removeUser(Integer.parseInt(userId));
    }

    @Test
    public void userControllerShouldSetNumberOfResultsInSessionAndRedirectBackToSamePage () throws Exception {
        String numberOfResults = "10";
        String pageNr = "12";
        HttpSession session = mockMvc.perform(post("/users/setNumberOfResults").param("numberOfResults", numberOfResults).param("pageNr", pageNr))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/" + pageNr))
                .andReturn().getRequest().getSession();
        assertNotNull(session);
        assertEquals(Integer.parseInt(numberOfResults), session.getAttribute("numberOfResults"));
    }
}
