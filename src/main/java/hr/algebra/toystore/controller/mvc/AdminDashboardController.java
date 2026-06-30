package hr.algebra.toystore.controller.mvc;

import hr.algebra.toystore.dto.ToyCategoryDto;
import hr.algebra.toystore.dto.ToyDto;
import hr.algebra.toystore.model.ToySearchForm;
import hr.algebra.toystore.service.LoginAuditService;
import hr.algebra.toystore.service.ToyCategoryService;
import hr.algebra.toystore.service.ToyService;
import hr.algebra.toystore.util.Constants;
import hr.algebra.toystore.util.StringFormatter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminDashboardController {

    private final ToyService toyService;
    private final ToyCategoryService toyCategoryService;
    private final LoginAuditService auditRepository;

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
        return Constants.REDIRECT_DASHBOARD;
    }

    @PostMapping("/toy")
    public String addToy(@ModelAttribute ToyDto toyForm, @RequestParam("image") MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        try {
            toyService.save(toyForm, file);
        }
        catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(Constants.ERROR_ATTRIBUTE, e.getMessage());
        }
        return Constants.REDIRECT_DASHBOARD;
    }

    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            toyCategoryService.deleteById(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constants.ERROR_ATTRIBUTE, "Toy category can't be deleted.");
        }
        return Constants.REDIRECT_DASHBOARD;
    }

    @PostMapping("/toy/delete/{id}")
    public String deleteToy(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            toyService.deleteById(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constants.ERROR_ATTRIBUTE, "Toy can't be deleted.");
        }
        return Constants.REDIRECT_DASHBOARD;
    }

    @PostMapping("/category/edit")
    public String editCategory(@ModelAttribute ToyCategoryDto categoryForm) {
        toyCategoryService.update(categoryForm.getId(), categoryForm);
        return Constants.REDIRECT_DASHBOARD;
    }

    @PostMapping("/toy/edit")
    public String editToy(@ModelAttribute ToyDto toyForm, @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        toyService.update(toyForm.getId(), toyForm, imageFile);
        return Constants.REDIRECT_DASHBOARD;
    }

    @PostMapping("/toy/search")
    @ResponseBody
    public List<ToyDto> searchToys(@ModelAttribute ToySearchForm form) {
        boolean isEmpty = (form.getName() == null || form.getName().isBlank())
                && (form.getDescription() == null || form.getDescription().isBlank())
                && form.getMinPrice() == null
                && form.getMaxPrice() == null
                && (form.getCategoryString() == null || form.getCategoryString().isBlank());

        if (!isEmpty && form.getCategoryString() != null && !form.getCategoryString().isBlank()) {
            form.setCategoryString(StringFormatter.formatToDb(form.getCategoryString()));
        }

        if (isEmpty) {
            return toyService.findAll();
        }
        return toyService.findByCriteria(form);
    }

    @GetMapping("/logins")
    public String viewLogins(Model model) {
        model.addAttribute("logins", auditRepository.findAll());
        return "admin/login_audit";
    }
}
