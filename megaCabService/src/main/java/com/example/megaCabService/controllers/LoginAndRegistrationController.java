package com.example.megaCabService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.megaCabService.components.SessionData;
import com.example.megaCabService.models.ClientModel;
import com.example.megaCabService.repositories.ClientRepository;
import com.example.megaCabService.repositories.LoginAndRegistrationRepository;

@Controller
@RequestMapping("/user")
public class LoginAndRegistrationController {
    
    @Autowired
    private LoginAndRegistrationRepository loginAndRegistrationRepository;
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private SessionData sessionData; 

    @GetMapping({"", "/"})
    public String index(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "index";
    }
    
    @GetMapping("/register")
    public String register() {
        return "clientForm";
    }
    
    @GetMapping("/clientHome")
    public String clientHome(Model model) {
        if (sessionData.getLoggedClient() == null) {
            return "redirect:/"; 
        }
        model.addAttribute("loggedClient", sessionData.getLoggedClient());
        return "clientHome";
    }
    
    @PostMapping("/add")
    public String createClient(@ModelAttribute ClientModel client, RedirectAttributes redirectAttributes) {
        if (clientRepository.nicExists(client.getNic())) {
            redirectAttributes.addFlashAttribute("error", "Client NIC already exists!");
            return "redirect:/user/register";
        }
        if (clientRepository.usernameExists(client.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username already exists!");
            return "redirect:/user/register";
        }
        if (clientRepository.emailExists(client.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists!");
            return "redirect:/user/register";
        }

        ClientModel newClient = loginAndRegistrationRepository.clientRegistration(client);
        
        if (newClient == null) {
            redirectAttributes.addFlashAttribute("error", "Registration failed!");
            return "redirect:/user/register";
        }

        redirectAttributes.addFlashAttribute("success", "Registration Successful");
        return "redirect:/user/register";  
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {

        if ("admin".equals(username) && "1234".equals(password)) {
            return "redirect:/admin/dashboard";
        } else {
            if (!clientRepository.usernameExists(username)) {
                redirectAttributes.addFlashAttribute("error", "Incorrect username or password");
                return "redirect:/";
            } else {
                String storedPassword = loginAndRegistrationRepository.getPasswordByUsername(username);

                if (!storedPassword.equals(password)) {
                    redirectAttributes.addFlashAttribute("error", "Incorrect password!");
                    return "redirect:/";
                } else {
                    ClientModel client = clientRepository.getClientByUsername(username);
                    sessionData.setLoggedClient(client); 
                    
                    return "redirect:/user/clientHome"; 
                }
            }
        }
    }
}
