package saul.com.task2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.engine.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import saul.com.task2.entity.UserEntity;
import saul.com.task2.service.UserService;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;



@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {
        when(userService.getUsers()).thenReturn(Arrays.asList(
                new UserEntity("Saul", "Colin"),
                new UserEntity("Alejandro", "Salas")
        ));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].name").value("Saul"))
                .andExpect(jsonPath("$[1].name").value("Alejandro"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateUser() throws Exception{
        UserEntity user = new UserEntity("Saul", "Colin");
        when(userService.createUser(any(UserEntity.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .with(csrf())) // Add CSRF token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Saul"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteNotAdminUser() throws Exception{
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());
    }


}