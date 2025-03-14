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

import com.example.megaCabService.components.SessionData;
import com.example.megaCabService.models.ClientModel;
import com.example.megaCabService.repositories.ClientRepository;

@Controller
@RequestMapping("/clients")
public class ClientController {

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private SessionData sessionData;
	
    @GetMapping
    public String getAllClients(Model model) {
        List<ClientModel> clients = clientRepository.getAllClients();
        model.addAttribute("clients", clients);
        return "clients";
    }
    
    @GetMapping("/ownProfile")
    public String getClient(Model model, RedirectAttributes redirectAttributes) {
        ClientModel client = clientRepository.getClient(sessionData.getLoggedClient().getClientID());
        
        if (client != null) {
            model.addAttribute("client", client);
            return "editProfile"; 
        } else {
            redirectAttributes.addFlashAttribute("error", "Client not found!");
            return "redirect:/";
        }
    }
    
    @GetMapping("/delete/{clientID}")
    public String deleteClient(@PathVariable int clientID, RedirectAttributes redirectAttributes) {
        ClientModel client = clientRepository.getClient(clientID);
        if (client != null) {
            clientRepository.deleteClient(clientID);
            redirectAttributes.addFlashAttribute("success", "Client deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Client not found!");
        }
        return "redirect:/clients";
    }
    
    @GetMapping("/edit/{clientID}")
    public String editClient(@PathVariable int clientID, Model model) {
        ClientModel client = clientRepository.getClient(clientID);
        
        if (client != null) {
            model.addAttribute("client", client);
            return "editClientForm"; 
        }
        return "redirect:/clients";
    }

    @PostMapping("/update")
    public String updateClient(@ModelAttribute ClientModel client, RedirectAttributes redirectAttributes) {
        ClientModel existingClient = clientRepository.getClient(client.getClientID());
        
        if (!existingClient.getNic().equals(client.getNic())) {
            if (clientRepository.nicExists(client.getNic())) {
                redirectAttributes.addFlashAttribute("error", "Client NIC already exists!");
                return "redirect:/clients";
            }
        }
        
        if (!existingClient.getUsername().equals(client.getUsername())) {
            if (clientRepository.usernameExists(client.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "Username already exists!");
                return "redirect:/clients";
            }
        }
        
        if (!existingClient.getEmail().equals(client.getEmail())) {
            if (clientRepository.emailExists(client.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email already exists!");
                return "redirect:/clients";
            }
        }
        
        boolean updated = clientRepository.updateClient(client);
        if (updated) {
            redirectAttributes.addFlashAttribute("success", "Client updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Client update failed!");
        }
        return "redirect:/clients";
    }
    
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute ClientModel client, RedirectAttributes redirectAttributes) {
        ClientModel existingClient = clientRepository.getClient(client.getClientID());
        
        if (!existingClient.getNic().equals(client.getNic())) {
            if (clientRepository.nicExists(client.getNic())) {
                redirectAttributes.addFlashAttribute("error", "Client NIC already exists!");
                return "redirect:/clients/ownProfile";
            }
        }
        
        if (!existingClient.getUsername().equals(client.getUsername())) {
            if (clientRepository.usernameExists(client.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "Username already exists!");
                return "redirect:/clients/ownProfile";
            }
        }
        
        if (!existingClient.getEmail().equals(client.getEmail())) {
            if (clientRepository.emailExists(client.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email already exists!");
                return "redirect:/clients/ownProfile";
            }
        }
        
        boolean updated = clientRepository.updateClient(client);
        if (updated) {
            redirectAttributes.addFlashAttribute("success", "Client updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Client update failed!");
        }
        return "redirect:/clients/ownProfile";
    }
}
