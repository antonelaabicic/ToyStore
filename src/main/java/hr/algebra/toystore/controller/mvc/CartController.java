package hr.algebra.toystore.controller.mvc;

import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.dto.CartItemDto;
import hr.algebra.toystore.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String viewCart(HttpServletRequest request, Model model) {
        String sessionId = request.getSession().getId();
        CartDto cart = cartService.getCart(sessionId);
        model.addAttribute("cart", cart);
        return "cart/view";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam("toyId") Integer toyId,
                          @RequestParam("quantity") Integer quantity,
                          @RequestParam(value = "categoryId", required = false) Integer categoryId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {
        String sessionId = request.getSession().getId();
        cartService.addItem(sessionId, toyId, quantity);

        redirectAttributes.addFlashAttribute("toastMessage", "Added to cart!");

        return "redirect:/store/toys" + (categoryId != null ? "?categoryId=" + categoryId : "");
    }


    @PostMapping("/update")
    public String updateQuantity(@RequestParam("itemId") Integer itemId, @RequestParam("quantity") Integer quantity,
                                 HttpServletRequest request)
    {
        String sessionId = request.getSession().getId();
        cartService.updateItemQuantity(sessionId, itemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam("itemId") Integer itemId, HttpServletRequest request)
    {
        String sessionId = request.getSession().getId();
        cartService.removeItem(sessionId, itemId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpServletRequest request)
    {
        String sessionId = request.getSession().getId();
        cartService.clearCart(sessionId);
        return "redirect:/cart";
    }

    @GetMapping("/count")
    @ResponseBody
    public int getCartItemCount(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return cartService.getCart(sessionId)
                .getItems()
                .stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
    }
}
