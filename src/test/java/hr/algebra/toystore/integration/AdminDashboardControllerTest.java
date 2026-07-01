package hr.algebra.toystore.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /admin/dashboard -> displays dashboard")
    void dashboard() throws Exception {
        mockMvc.perform(get("/admin/dashboard")
                        .with(user("pperic").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists("toys"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("toyForm"))
                .andExpect(model().attributeExists("categoryForm"));
    }

    @Test
    @DisplayName("POST /admin/category -> adds category")
    void addCategory() throws Exception {
        mockMvc.perform(post("/admin/category")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("name", "TEST_CATEGORY"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    @DisplayName("POST /admin/category/edit -> edits category")
    void editCategory() throws Exception {
        mockMvc.perform(post("/admin/category/edit")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "UPDATED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    @DisplayName("POST /admin/category/delete -> deletes category")
    void deleteCategory() throws Exception {
        mockMvc.perform(post("/admin/category/delete/6")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    @DisplayName("POST /admin/category/delete -> shows error when category cannot be deleted")
    void deleteCategoryFailure() throws Exception {
        mockMvc.perform(post("/admin/category/delete/1")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    @DisplayName("POST /admin/toy -> adds toy")
    void addToy() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "toy.png", "image/png", "image".getBytes());

        mockMvc.perform(multipart("/admin/toy")
                        .file(image)
                        .with(user("pperic").roles("ADMIN"))
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        })
                        .param("name", "Toy")
                        .param("description", "Integration Test")
                        .param("price", "19.99")
                        .param("categoryString", "OTHER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    @DisplayName("POST /admin/toy/edit -> edits toy")
    void editToy() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "", "application/octet-stream", new byte[0]);

        mockMvc.perform(multipart("/admin/toy/edit")
                        .file(image)
                        .with(user("pperic").roles("ADMIN"))
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        })
                        .param("id", "1")
                        .param("name", "Updated Toy")
                        .param("description", "Updated")
                        .param("price", "22.99")
                        .param("categoryString", "ACTION_FIGURES")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    @DisplayName("POST /admin/toy/delete -> deletes toy")
    void deleteToy() throws Exception {
        mockMvc.perform(post("/admin/toy/delete/1")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    @DisplayName("POST /admin/toy/search -> returns all toys")
    void searchAllToys() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @DisplayName("POST /admin/toy/search -> searches by name")
    void searchByName() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("name", "Classic"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @DisplayName("POST /admin/toy/search -> searches by category")
    void searchByCategory() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("categoryString", "Plushies"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @DisplayName("POST /admin/toy/search -> searches by name and category")
    void searchByNameAndCategory() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("name", "Classic")
                        .param("categoryString", "Plushies"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @DisplayName("POST /admin/toy/search -> searches by price")
    void searchByPrice() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("minPrice", "10")
                        .param("maxPrice", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @DisplayName("POST /admin/toy/search -> blank name")
    void searchBlankName() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("name", " "))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /admin/toy/search -> blank description")
    void searchBlankDescription() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("description", " "))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @DisplayName("POST /admin/toy/search -> name without category")
    void searchNameWithoutCategory() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("name", "Bear"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @DisplayName("POST /admin/toy/search -> description and category")
    void searchDescriptionAndCategory() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("description", "soft")
                        .param("categoryString", "Plushies"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @DisplayName("POST /admin/toy/search -> description only")
    void searchByDescription() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("description", "soft"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /admin/toy/search -> blank category")
    void searchBlankCategory() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("categoryString", " "))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /admin/toy/search -> minimum price")
    void searchMinPrice() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("minPrice", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /admin/toy/search -> maximum price")
    void searchMaxPrice() throws Exception {
        mockMvc.perform(post("/admin/toy/search")
                        .with(user("pperic").roles("ADMIN"))
                        .with(csrf())
                        .param("maxPrice", "20"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/logins -> displays audit log")
    void loginAudit() throws Exception {
        mockMvc.perform(get("/admin/logins")
                        .with(user("pperic").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login_audit"))
                .andExpect(model().attributeExists("logins"));
    }

    @Test
    @DisplayName("POST /admin/toy -> invalid category redirects with error")
    void addToyInvalidCategory() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "toy.png", "image/png", "image".getBytes());

        mockMvc.perform(multipart("/admin/toy")
                        .file(image)
                        .with(user("pperic").roles("ADMIN"))
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        })
                        .with(csrf())
                        .param("name", "Toy")
                        .param("description", "Integration Test")
                        .param("price", "20")
                        .param("categoryString", "NOT_EXISTING"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"))
                .andExpect(flash().attributeExists("error"));
    }
}