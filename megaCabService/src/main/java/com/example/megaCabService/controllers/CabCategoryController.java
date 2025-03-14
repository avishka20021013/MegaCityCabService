package com.example.megaCabService.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.megaCabService.models.CategoryModel;
import com.example.megaCabService.repositories.CabCategoryRepository;

@Controller
@RequestMapping("/cabCategory")
public class CabCategoryController {
    @Autowired
    private CabCategoryRepository cabCategoryRepository;

    @GetMapping
    public String getAllCategories(Model model) {
        List<CategoryModel> categories = cabCategoryRepository.getAllCategories();
        model.addAttribute("categories", categories);
        return "cabCategory";
    }

    @GetMapping("/create")
    public String create() {
        return "cabCategoryForm";
    }

    @PostMapping("/add")
    public String createCategory(@ModelAttribute CategoryModel categoryModel, RedirectAttributes redirectAttributes) {
        if (cabCategoryRepository.categoryExists(categoryModel.getCategory())) {
            redirectAttributes.addFlashAttribute("error", "Category already exists!");
            return "redirect:/cabCategory/create";
        }

        CategoryModel newCategory = cabCategoryRepository.addCategory(categoryModel);
        
        if (newCategory == null) {
            redirectAttributes.addFlashAttribute("error", "Failed to add category");
            return "redirect:/cabCategory/create";
        }

        redirectAttributes.addFlashAttribute("success", "Adding Successful");
        return "redirect:/cabCategory/create";  
    }
    
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") int id, Model model) {
        CategoryModel category = cabCategoryRepository.getCategory(id);
        if (category == null) {
            return "redirect:/cabCategory";
        }
        model.addAttribute("category", category);
        return "editCategories"; 
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute CategoryModel categoryModel, RedirectAttributes redirectAttributes) {
        CategoryModel existingCategory = cabCategoryRepository.getCategoryByName(categoryModel.getCategory());

        if (existingCategory != null && existingCategory.getCategoryID() != categoryModel.getCategoryID()) {
            redirectAttributes.addFlashAttribute("error", "Category already exists!");
            return "redirect:/cabCategory/edit/" + categoryModel.getCategoryID();
        }

        boolean updated = cabCategoryRepository.updateCabCategory(categoryModel);
        if (updated) {
            redirectAttributes.addFlashAttribute("success", "Category updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update category!");
        }
        return "redirect:/cabCategory";
    }


    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        boolean deleted = cabCategoryRepository.deleteCabCategory(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete category!");
        }
        return "redirect:/cabCategory";
    }

}
