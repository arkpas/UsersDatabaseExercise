package integration.controller;

import arkpas.userdatabase.MainApp;
import arkpas.userdatabase.controller.FileController;
import arkpas.userdatabase.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest (classes = MainApp.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private FileController fileController;

    @Before
    public void setup () {
        fileController = new FileController(userService);
    }

    @Test
    public void fileUploadTest () throws Exception {
        MockMultipartFile file = new MockMultipartFile("file","original", null, "content".getBytes());

        mockMvc.perform(multipart("/users/fileUpload").file(file))
                .andExpect(view().name("redirect:/users"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(userService, times(1)).saveUsers(any());
    }

}
