package com.example.megaCabService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.megaCabService.repositories.BookingRepository;
import com.example.megaCabService.repositories.CabsRepository;
import com.example.megaCabService.repositories.ClientRepository;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
	@Autowired
    private BookingRepository bookingRepository;
	@Autowired
    private ClientRepository clientRepository;
	@Autowired
    private CabsRepository cabsRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        int bookingCount = bookingRepository.getBookingCount();
        int clientCount = clientRepository.getClientCount();
        int cabCount = cabsRepository.getCabCount();
        model.addAttribute("bookingCount", bookingCount);
        model.addAttribute("clientCount", clientCount);
        model.addAttribute("cabCount", cabCount);
        return "adminHome";
    }
}
