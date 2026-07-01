package hr.algebra.toystore.integration;

import hr.algebra.toystore.util.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /orders/checkout -> returns checkout page")
    void checkoutPage() throws Exception {
        mockMvc.perform(get("/orders/checkout")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("order/checkout"))
                .andExpect(model().attributeExists("paymentMethods"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("cart"));
    }

    @Test
    @DisplayName("POST /orders/checkout -> places order")
    void checkoutPost() throws Exception {
        mockMvc.perform(post("/orders/checkout")
                        .with(user("hhanic").roles("USER"))
                        .param("paymentMethodId", "1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/my"));
    }

    @Test
    @DisplayName("GET /orders/my -> shows user orders")
    void myOrders() throws Exception {
        mockMvc.perform(get("/orders/my")
                        .with(user("hhanic").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("order/my_orders"))
                .andExpect(model().attributeExists(Constants.ORDERS));
    }

    @Test
    @DisplayName("GET /orders/all -> shows search page")
    void allOrdersPage() throws Exception {
        mockMvc.perform(get("/orders/all")
                        .with(user("pperic").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name(Constants.ALL_ORDERS))
                .andExpect(model().attributeExists("searchForm"))
                .andExpect(model().attributeExists(Constants.ORDERS));
    }

    @Test
    @DisplayName("POST /orders/all -> valid search")
    void searchOrdersValid() throws Exception {
        mockMvc.perform(post("/orders/all")
                        .with(user("pperic").roles("ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(Constants.ALL_ORDERS))
                .andExpect(model().attributeExists(Constants.ORDERS));
    }

    @Test
    @DisplayName("POST /orders/all -> invalid dates")
    void searchOrdersInvalid() throws Exception {
        mockMvc.perform(post("/orders/all")
                        .with(user("pperic").roles("ADMIN"))
                        .param("fromDate", "2025-12-31")
                        .param("toDate", "2025-01-01")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(Constants.ALL_ORDERS))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists(Constants.ORDERS));
    }

    @Test
    @DisplayName("GET /orders/paypal/cancel -> cancel page")
    void paypalCancel() throws Exception {
        mockMvc.perform(get("/orders/paypal/cancel")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("order/paypal_cancel"));
    }

    @Test
    @DisplayName("GET /orders/success -> success page")
    void successPage() throws Exception {
        mockMvc.perform(get("/orders/success")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("order/paypal_success"));
    }

    @Test
    @DisplayName("GET /orders/error -> error page")
    void errorPage() throws Exception {
        mockMvc.perform(get("/orders/error")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("order/paypal_error"));
    }
}
