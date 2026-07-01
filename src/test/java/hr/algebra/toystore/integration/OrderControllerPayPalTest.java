package hr.algebra.toystore.integration;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import hr.algebra.toystore.service.PayPalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerPayPalTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PayPalService payPalService;

    @Test
    @DisplayName("GET /orders/paypal/pay -> redirects to PayPal approval URL")
    void paypalRedirectSuccess() throws Exception {
        Links approval = new Links();
        approval.setRel("approval_url");
        approval.setHref("https://paypal.test/approve");

        Payment payment = new Payment();
        payment.setLinks(List.of(approval));

        when(payPalService.createPayment(anyDouble(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString())).thenReturn(payment);

        mockMvc.perform(get("/orders/paypal/pay")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://paypal.test/approve"));
    }

    @Test
    @DisplayName("GET /orders/paypal/pay -> redirects back to checkout when approval URL missing")
    void paypalRedirectWithoutApprovalUrl() throws Exception {
        Payment payment = new Payment();
        payment.setLinks(List.of());

        when(payPalService.createPayment(anyDouble(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString())).thenReturn(payment);

        mockMvc.perform(get("/orders/paypal/pay")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/checkout?error"));
    }

    @Test
    @DisplayName("GET /orders/paypal/pay -> PayPal exception redirects to checkout")
    void paypalRedirectException() throws Exception {
        when(payPalService.createPayment(anyDouble(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString())).thenThrow(new PayPalRESTException("boom"));

        mockMvc.perform(get("/orders/paypal/pay")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/checkout?error"));
    }

    @Test
    @DisplayName("GET /orders/paypal/success -> approved payment")
    void paypalSuccessApproved() throws Exception {
        Payment payment = new Payment();
        payment.setState("approved");

        when(payPalService.executePayment(anyString(), anyString())).thenReturn(payment);

        mockMvc.perform(get("/orders/paypal/success")
                        .param("paymentId", "1")
                        .param("PayerID", "2")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/success"));
    }

    @Test
    @DisplayName("GET /orders/paypal/success -> payment not approved")
    void paypalSuccessNotApproved() throws Exception {
        Payment payment = new Payment();
        payment.setState("failed");

        when(payPalService.executePayment(anyString(), anyString())).thenReturn(payment);

        mockMvc.perform(get("/orders/paypal/success")
                        .param("paymentId", "1")
                        .param("PayerID", "2")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/error"));
    }

    @Test
    @DisplayName("GET /orders/paypal/success -> PayPal exception")
    void paypalSuccessException() throws Exception {
        when(payPalService.executePayment(anyString(), anyString())).thenThrow(new PayPalRESTException("boom"));

        mockMvc.perform(get("/orders/paypal/success")
                        .param("paymentId", "1")
                        .param("PayerID", "2")
                        .with(user("aanic").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/error"));
    }
}