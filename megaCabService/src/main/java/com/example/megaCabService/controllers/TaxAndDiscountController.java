package com.example.megaCabService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.megaCabService.models.TaxAndDiscountModel;
import com.example.megaCabService.repositories.TaxAndDiscountRepository;

@Controller
@RequestMapping("/taxDiscount")
public class TaxAndDiscountController {

	@Autowired
	private TaxAndDiscountRepository taxAndDiscountRepository;
	
	public void ensureDefaultTaxAndDiscount() {
        taxAndDiscountRepository.addDefaultTaxAndDiscount();
    }
	
	@GetMapping
	public String getTaxAndDiscount(Model model) {
	    taxAndDiscountRepository.addDefaultTaxAndDiscount();

	    TaxAndDiscountModel taxAndDiscount = taxAndDiscountRepository.getTaxAndDiscount(1);
	    model.addAttribute("taxAndDiscount", taxAndDiscount);
	    return "taxAndDiscount";
	}


	 @PostMapping("/update")
	    public String updateTaxAndDiscount(@ModelAttribute TaxAndDiscountModel taxAndDiscountModel, RedirectAttributes redirectAttributes) {
	        boolean updated = taxAndDiscountRepository.updateTaxAndDiscount(taxAndDiscountModel);
	        
	        if (updated) {
	            redirectAttributes.addFlashAttribute("success", "Tax and discount updated successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("error", "Failed to update tax and discount!");
	        }
	        return "redirect:/taxDiscount";
	    }
}
