package hr.algebra.toystore.controller.mvc;

import hr.algebra.toystore.dto.ToyCategoryDto;
import hr.algebra.toystore.dto.ToyDto;
import hr.algebra.toystore.model.ToySearchForm;
import hr.algebra.toystore.service.ToyCategoryService;
import hr.algebra.toystore.service.ToyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/store")
@AllArgsConstructor
public class ToyStoreController {

    private final ToyService toyService;
    private final ToyCategoryService toyCategoryService;

    @GetMapping("/toys")
    public String showCategorySelection(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                        Model model) {
        List<ToyCategoryDto> categories = toyCategoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);

        List<ToyDto> toys = (categoryId != null) ? toyService.findByCategoryId(categoryId) : List.of();
        model.addAttribute("toys", toys);

        return "store/toys";
    }

    @GetMapping("/toys/cards")
    public String getToyCards(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                              Model model) {
        List<ToyDto> toys = (categoryId != null) ? toyService.findByCategoryId(categoryId) : List.of();
        List<ToyCategoryDto> categories = toyCategoryService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("toys", toys);

        return "store/toys :: #ajaxCardsPlaceholder";
    }

    @PostMapping("/toys/search")
    public String searchToys(@ModelAttribute ToySearchForm form, Model model) {
        List<ToyDto> toys = toyService.findByCriteria(form);

        List<ToyCategoryDto> categories = toyCategoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", form.getCategoryId());
        model.addAttribute("toys", toys);

        return "store/toys :: #ajaxCardsPlaceholder";
    }
}
