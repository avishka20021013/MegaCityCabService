package com.example.megaCabService.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.example.megaCabService.models.CabModel;
import com.example.megaCabService.repositories.CabsRepository;

@Controller
@RequestMapping("/cabs")
public class CabsController {
	
    @Autowired
    private CabsRepository cabsRepository;
	
    @GetMapping("/newCab")
    public String newCab(Model model) {
        List<String> categories = cabsRepository.getAllCabCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("cab", new CabModel());
        return "cabForm";
    }

	
    @PostMapping("/add")
    public String addCab(@ModelAttribute CabModel cab, 
                         @RequestParam("file") MultipartFile file, 
                         RedirectAttributes redirectAttributes) {
        
        if (!file.isEmpty()) {
            try {
                String uploadDir = "uploads/"; 
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                cab.setImageUrl(fileName); // Save only the filename in DB
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "File upload failed!");
                return "redirect:/cabs/newCab";
            }
        }

        CabModel newCab = cabsRepository.addCab(cab);
        if (newCab == null) {
            redirectAttributes.addFlashAttribute("error", "Cab number already exists!");
            return "redirect:/cabs/newCab";
        }
        redirectAttributes.addFlashAttribute("success", "Cab Added Successfully");
        return "redirect:/cabs/newCab";
    }

	
    @GetMapping
    public String getAllCabs(Model model) {
        List<CabModel> cabs = cabsRepository.getAllCabs();
        model.addAttribute("cabs", cabs);
        return "cabs";
    }
    

    @GetMapping("/delete/{cabID}")
    public String deleteCab(@PathVariable int cabID, RedirectAttributes redirectAttributes) {
        CabModel cab = cabsRepository.getCab(cabID);

        if (cab != null) {
            cabsRepository.deleteCab(cabID);
            redirectAttributes.addFlashAttribute("success", "Cab deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cab not found!");
        }

        return "redirect:/cabs";
    }




    @GetMapping("/edit/{cabID}")
    public String editCab(@PathVariable int cabID, Model model) {
        CabModel cab = cabsRepository.getCab(cabID);
        List<String> categories = cabsRepository.getAllCabCategories(); 
        
        if (cab != null) {
            model.addAttribute("cab", cab);
            model.addAttribute("categories", categories);
            return "editCabForm"; 
        }
        return "redirect:/cabs";
    }

    @PostMapping("/update")
    public String updateCab(@ModelAttribute CabModel cab, 
                            @RequestParam(value = "file", required = false) MultipartFile file, 
                            RedirectAttributes redirectAttributes) {

        CabModel existingCab = cabsRepository.getCab(cab.getCabID());

        if (existingCab == null) {
            redirectAttributes.addFlashAttribute("error", "Cab not found!");
            return "redirect:/cabs";
        }

        if (file != null && !file.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                cab.setImageUrl(fileName);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Error uploading file!");
                return "redirect:/cabs/edit/" + cab.getCabID();
            }
        } else {
            cab.setImageUrl(existingCab.getImageUrl());
        }

        boolean updated = cabsRepository.updateCab(cab);

        if (updated) {
            redirectAttributes.addFlashAttribute("success", "Cab updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cab update failed!");
        }

        return "redirect:/cabs";
    }


}

