package hr.algebra.toystore.controller.mvc;

import hr.algebra.toystore.dto.ToyCategoryDto;
import hr.algebra.toystore.dto.ToyDto;
import hr.algebra.toystore.model.ToySearchForm;
import hr.algebra.toystore.service.ToyCategoryService;
import hr.algebra.toystore.service.ToyService;
import hr.algebra.toystore.util.StringFormatter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
@SessionAttributes({"toyForm", "categoryForm"})
public class AdminDashboardController {

    private final ToyService toyService;
    private final ToyCategoryService toyCategoryService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("toys", toyService.findAll());
        model.addAttribute("categories", toyCategoryService.findAll());

        model.addAttribute("toyForm", new ToyDto());
        model.addAttribute("categoryForm", new ToyCategoryDto());

        return "admin/dashboard";
    }

    @PostMapping("/category")
    public String addCategory(@ModelAttribute ToyCategoryDto categoryForm) {
        toyCategoryService.save(categoryForm);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/toy")
    public String addToy(@ModelAttribute ToyDto toyForm, @RequestParam("image") MultipartFile file) {
        toyService.save(toyForm, file);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        toyCategoryService.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/toy/delete/{id}")
    public String deleteToy(@PathVariable Integer id) {
        toyService.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/category/edit")
    public String editCategory(@ModelAttribute ToyCategoryDto categoryForm) {
        toyCategoryService.update(categoryForm.getId(), categoryForm);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/toy/edit")
    public String editToy(@ModelAttribute ToyDto toyForm, @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            toyService.updateWithImage(toyForm.getId(), toyForm, imageFile);
        } else {
            toyService.update(toyForm.getId(), toyForm);
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/toy/search")
    @ResponseBody
    public List<ToyDto> searchToys(@ModelAttribute ToySearchForm form) {
        boolean isEmpty = (form.getName() == null || form.getName().isBlank())
                && (form.getDescription() == null || form.getDescription().isBlank())
                && form.getMinPrice() == null
                && form.getMaxPrice() == null
                && form.getInStock() == null
                && (form.getCategoryString() == null || form.getCategoryString().isBlank());

        if (!isEmpty && form.getCategoryString() != null && !form.getCategoryString().isBlank()) {
            form.setCategoryString(StringFormatter.formatToDb(form.getCategoryString()));
        }

        if (isEmpty) {
            return toyService.findAll();
        }
        return toyService.findByCriteria(form);
    }
}
