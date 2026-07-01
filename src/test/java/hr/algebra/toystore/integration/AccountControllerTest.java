package hr.algebra.toystore.integration;

import hr.algebra.toystore.dto.UserRegisterDto;
import hr.algebra.toystore.service.ApplicationUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplicationUserService applicationUserService;

    @Test
    @DisplayName("GET /user/login -> displays login page")
    void loginPage() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    @DisplayName("GET /user/register -> displays register page")
    void registerPage() throws Exception {
        mockMvc.perform(get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("POST /user/register -> successful registration")
    void registerSuccess() throws Exception {
        doNothing().when(applicationUserService).registerNewUser(any(UserRegisterDto.class));

        mockMvc.perform(post("/user/register")
                        .with(csrf())
                        .param("username", "newuser")
                        .param("password", "123")
                        .param("confirmPassword", "123")
                        .param("name", "John")
                        .param("surname", "Doe")
                        .param("email", "john@test.com")
                        .param("country", "Croatia")
                        .param("city", "Zagreb")
                        .param("address", "Main Street"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

    @Test
    @DisplayName("POST /user/register -> passwords do not match")
    void registerPasswordMismatch() throws Exception {
        mockMvc.perform(post("/user/register")
                        .with(csrf())
                        .param("username", "newuser")
                        .param("password", "123")
                        .param("confirmPassword", "456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/register"))
                .andExpect(flash().attribute("error", "Passwords do not match."));
    }

    @Test
    @DisplayName("POST /user/register -> registration service throws exception")
    void registerServiceException() throws Exception {
        doThrow(new IllegalArgumentException("Username already exists."))
                .when(applicationUserService)
                .registerNewUser(any(UserRegisterDto.class));

        mockMvc.perform(post("/user/register")
                        .with(csrf())
                        .param("username", "existing")
                        .param("password", "123")
                        .param("confirmPassword", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/register"))
                .andExpect(flash().attribute("error", "Username already exists."));
    }
}