package hr.algebra.toystore.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.toystore.dto.JwtResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApiIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/auth/login returns JWT tokens")
    void loginSuccess() throws Exception {
        JwtResponseDto jwt = login("pperic", "123");

        assertNotNull(jwt);
        assertNotNull(jwt.getAccessToken());
        assertNotNull(jwt.getRefreshToken());
    }

    @Test
    @DisplayName("POST /api/auth/login with wrong password returns 401")
    void loginWrongPassword() throws Exception {
        String json = """
                {
                  "username":"pperic",
                  "password":"wrong"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/logout with JWT returns 204")
    void logoutSuccess() throws Exception {
        JwtResponseDto jwt = login("pperic", "123");
        mockMvc.perform(post("/api/auth/logout")
               .header("Authorization", "Bearer " + jwt.getAccessToken()))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/auth/logout without JWT returns 401")
    void logoutUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/logout")).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/refresh with invalid token returns error")
    void refreshInvalidToken() throws Exception {
        String json = """
            {
              "refreshToken":"invalid-token"
            }
            """;
        mockMvc.perform(post("/api/auth/refresh")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/refresh returns new tokens")
    void refreshSuccess() throws Exception {
        JwtResponseDto jwt = login("pperic", "123");
        String json = """
            {
              "refreshToken":"%s"
            }
            """.formatted(jwt.getRefreshToken());

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("POST /api/auth/revoke/{id} as admin")
    void revokeAsAdmin() throws Exception {
        JwtResponseDto jwt = login("pperic", "123");
        mockMvc.perform(post("/api/auth/revoke/2")
               .header("Authorization", "Bearer " + jwt.getAccessToken()))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/auth/revoke/{id} as user returns 403")
    void revokeAsUserForbidden() throws Exception {
        JwtResponseDto jwt = login("aanic", "123");
        mockMvc.perform(post("/api/auth/revoke/1")
                .header("Authorization", "Bearer " + jwt.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/auth/revoke/{id} without JWT returns 401")
    void revokeWithoutToken() throws Exception {
        mockMvc.perform(post("/api/auth/revoke/1")).andExpect(status().isUnauthorized());
    }

    private JwtResponseDto login(String username, String password) throws Exception {

        String json = """
                {
                  "username":"%s",
                  "password":"%s"
                }
                """.formatted(username, password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                JwtResponseDto.class
        );
    }
}
