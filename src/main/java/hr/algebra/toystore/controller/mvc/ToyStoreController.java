package hr.algebra.toystore.controller.mvc;

import hr.algebra.toystore.dto.ToyCategoryDto;
import hr.algebra.toystore.service.ToyCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/store")
@AllArgsConstructor
public class ToyStoreController {
    private final ToyCategoryService toyCategoryService;

    @GetMapping("/toys")
    public String showCategories(Model model) {
        List<ToyCategoryDto> categories = toyCategoryService.findAll();
        model.addAttribute("categories", categories);
        return "store/toys";
    }
}
