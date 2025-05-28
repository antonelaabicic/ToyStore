package hr.algebra.toystore.controller.mvc;

import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.dto.CartItemDto;
import hr.algebra.toystore.service.CartService;
import hr.algebra.toystore.service.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserSessionService userSessionService;

    private String getSessionId() {
        return userSessionService.getCurrentSessionId();
    }

    @GetMapping
    public String viewCart(Model model) {
        CartDto cart = cartService.getCart(getSessionId());
        model.addAttribute("cart", cart);
        return "cart/view";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam("toyId") Integer toyId,
                          @RequestParam("quantity") Integer quantity,
                          @RequestParam(value = "categoryId", required = false) Integer categoryId,
                          RedirectAttributes redirectAttributes) {

        cartService.addItem(getSessionId(), toyId, quantity);
        redirectAttributes.addFlashAttribute("toastMessage", "Added to cart!");
        return "redirect:/store/toys" + (categoryId != null ? "?categoryId=" + categoryId : "");
    }


    @PostMapping("/update")
    public String updateQuantity(@RequestParam("itemId") Integer itemId, @RequestParam("quantity") Integer quantity)
    {
        cartService.updateItemQuantity(getSessionId(), itemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam("itemId") Integer itemId)
    {
        cartService.removeItem(getSessionId(), itemId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart()
    {
        cartService.clearCart(getSessionId());
        return "redirect:/cart";
    }

    @GetMapping("/count")
    @ResponseBody
    public int getCartItemCount() {
        return cartService.getCart(getSessionId())
                .getItems()
                .stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
    }
}
