package hr.algebra.toystore.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ToyStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /store/toys -> displays categories")
    void showCategories() throws Exception {
        mockMvc.perform(get("/store/toys"))
                .andExpect(status().isOk())
                .andExpect(view().name("store/toys"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("toys"));
    }

    @Test
    @DisplayName("GET /store/toys with category -> displays toys")
    void showCategory() throws Exception {
        mockMvc.perform(get("/store/toys")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("store/toys"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("selectedCategoryId"))
                .andExpect(model().attributeExists("toys"));
    }

    @Test
    @DisplayName("GET /store/toys/cards -> returns fragment")
    void toyCardsWithoutCategory() throws Exception {
        mockMvc.perform(get("/store/toys/cards"))
                .andExpect(status().isOk())
                .andExpect(view().name("store/toys :: #ajaxCardsPlaceholder"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("toys"));
    }

    @Test
    @DisplayName("GET /store/toys/cards with category -> returns filtered fragment")
    void toyCardsWithCategory() throws Exception {
        mockMvc.perform(get("/store/toys/cards")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("store/toys :: #ajaxCardsPlaceholder"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("selectedCategoryId"))
                .andExpect(model().attributeExists("toys"));
    }

    @Test
    @DisplayName("POST /store/toys/search -> searches toys")
    void searchToys() throws Exception {
        mockMvc.perform(post("/store/toys/search")
                        .with(csrf())
                        .param("name", "Classic")
                        .param("categoryId", "4")
                        .param("minPrice", "10")
                        .param("maxPrice", "20"))
                .andExpect(status().isOk())
                .andExpect(view().name("store/toys :: #ajaxCardsPlaceholder"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("selectedCategoryId"))
                .andExpect(model().attributeExists("toys"));
    }
}