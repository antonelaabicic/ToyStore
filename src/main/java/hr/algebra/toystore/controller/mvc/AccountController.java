package hr.algebra.toystore.controller.mvc;

import hr.algebra.toystore.dto.UserRegisterDto;
import hr.algebra.toystore.service.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class AccountController {
    private final ApplicationUserService userService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegisterDto());
        return "user/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") UserRegisterDto dto, RedirectAttributes redirectAttributes) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/user/register";
        }

        try {
            userService.registerNewUser(dto);
            return "redirect:/user/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/register";
        }
    }
}
