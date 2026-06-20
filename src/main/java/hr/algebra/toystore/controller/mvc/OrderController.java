package hr.algebra.toystore.controller.mvc;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.dto.PaymentMethodDto;
import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.model.OrderSearchForm;
import hr.algebra.toystore.service.*;
import hr.algebra.toystore.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderSearchFormValidationService orderSearchFormValidator;
    private final PaymentMethodService paymentMethodService;
    private final ApplicationUserService applicationUserService;
    private final UserSessionService userSessionService;
    private final PayPalService payPalService;

    private String getSessionId() {
        return userSessionService.getCurrentSessionId();
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, Principal principal) {
        model.addAttribute("paymentMethods", paymentMethodService.getAll());
        model.addAttribute("user", applicationUserService.findByUsername(principal.getName()));
        model.addAttribute("cart", orderService.getCartForCheckout(getSessionId()));

        return "order/checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam("paymentMethodId") Integer paymentMethodId,
                             Principal principal) {
        PaymentMethodDto paymentMethod = paymentMethodService.findById(paymentMethodId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method ID"));

        UserDto user = applicationUserService.findByUsername(principal.getName());

        orderService.placeOrder(getSessionId(), paymentMethod, user);
        return "redirect:/orders/my";
    }

    @GetMapping("/my")
    public String viewUserOrders(Principal principal, Model model) {
        UserDto userDto = applicationUserService.findByUsername(principal.getName());
        model.addAttribute(Constants.ORDERS, orderService.getOrdersByUser(userDto));
        return "order/my_orders";
    }

    @ModelAttribute("searchForm")
    public OrderSearchForm searchForm() {
        return new OrderSearchForm();
    }

    @PostMapping("/all")
    public String viewAllOrders(
            @ModelAttribute("searchForm") OrderSearchForm searchForm,
            Model model
    ) {
        if (!orderSearchFormValidator.isValid(searchForm)) {
            model.addAttribute("error", "Start date must be before end date.");
            model.addAttribute(Constants.ORDERS, List.of());
            return "order/all_orders";
        }

        model.addAttribute(Constants.ORDERS, orderService.searchOrders(searchForm));
        return "order/all_orders";
    }

    @GetMapping("/all")
    public String showAllOrdersPage(Model model) {
        model.addAttribute("searchForm", new OrderSearchForm());
        model.addAttribute(Constants.ORDERS, List.of());
        return "order/all_orders";
    }

    @GetMapping("/paypal/pay")
    public String redirectToPayPal(HttpServletRequest request) {
        CartDto cart = orderService.getCartForCheckout(getSessionId());
        double total = cart.getTotalPrice().doubleValue();

        String baseUrl = request.getScheme() + "://" + request.getServerName() +
                (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort());

        try {
            Payment payment = payPalService.createPayment(
                    total,
                    "EUR",
                    "paypal",
                    "sale",
                    "Toy Store Order",
                    baseUrl + "/orders/paypal/cancel",
                    baseUrl + "/orders/paypal/success"
            );

            for (Links link : payment.getLinks()) {
                if ("approval_url".equals(link.getRel())) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error("PayPal payment failed: {}", e.getMessage());
        }

        return "redirect:/orders/checkout?error";
    }

    @GetMapping("/paypal/success")
    public String handlePayPalSuccess(@RequestParam("paymentId") String paymentId,
                                      @RequestParam("PayerID") String payerId,
                                      Principal principal) {

        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);

            if ("approved".equals(payment.getState())) {
                UserDto user = applicationUserService.findByUsername(principal.getName());
                PaymentMethodDto paypalMethod = paymentMethodService.findByName("PAYPAL");

                orderService.placeOrder(getSessionId(), paypalMethod, user);
                return "redirect:/orders/success";
            }
        } catch (PayPalRESTException e) {
            log.error("PayPal payment failed: {}", e.getMessage());
        }

        return "redirect:/orders/error";
    }

    @GetMapping("/paypal/cancel")
    public String handlePayPalCancel() {
        return "order/paypal_cancel";
    }

    @GetMapping("/success")
    public String showOrderSuccessPage() {
        return "order/paypal_success";
    }

    @GetMapping("/error")
    public String showOrderErrorPage() {
        return "order/paypal_error";
    }
}
