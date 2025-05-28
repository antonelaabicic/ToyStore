package hr.algebra.toystore.controller.mvc;

import hr.algebra.toystore.dto.PaymentMethodDto;
import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.model.OrderSearchForm;
import hr.algebra.toystore.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderSearchFormValidationService orderSearchFormValidator;
    private final PaymentMethodService paymentMethodService;
    private final ApplicationUserService applicationUserService;
    private final UserSessionService userSessionService;

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
        model.addAttribute("orders", orderService.getOrdersByUser(userDto));
        return "order/my_orders";
    }

    @ModelAttribute("searchForm")
    public OrderSearchForm searchForm() {
        return new OrderSearchForm();
    }

    @GetMapping("/all")
    public String viewAllOrders(
            @ModelAttribute("searchForm") OrderSearchForm searchForm,
            Model model
    ) {
        if (!orderSearchFormValidator.isValid(searchForm)) {
            model.addAttribute("error", "Start date must be before end date.");
            model.addAttribute("orders", List.of());
            return "order/all_orders";
        }

        model.addAttribute("orders", orderService.searchOrders(searchForm));
        return "order/all_orders";
    }
}
