package com.example.megaCabService.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.megaCabService.components.SessionData;
import com.example.megaCabService.models.BookingModel;
import com.example.megaCabService.models.CabModel;
import com.example.megaCabService.models.CategoryModel;
import com.example.megaCabService.models.TaxAndDiscountModel;
import com.example.megaCabService.repositories.BookingRepository;
import com.example.megaCabService.repositories.CabCategoryRepository;
import com.example.megaCabService.repositories.CabsRepository;
import com.example.megaCabService.repositories.TaxAndDiscountRepository;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private SessionData sessionData;
    @Autowired
    private CabsRepository cabsRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CabCategoryRepository cabCategoryRepository;
    @Autowired
    private TaxAndDiscountRepository taxAndDiscountRepository;


    @GetMapping
    public String addBookings(Model model) {
        model.addAttribute("sessionData", sessionData);
        List<String> categories = cabsRepository.getAllCabCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("cab", new CabModel());

        model.addAttribute("selectedCategory", "");

        return "addBooking";
    }

    @GetMapping("/filter")
    public String filterCabsByCategoryAndDates(@RequestParam String category, 
                                               @RequestParam String dates, 
                                               Model model) {
        model.addAttribute("sessionData", sessionData);

        List<String> categories = cabsRepository.getAllCabCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);

        List<String> selectedDates = List.of(dates.split(","));
        model.addAttribute("selectedDates", dates);

        List<CabModel> availableCabs = bookingRepository.getAvailableCabsByDates(selectedDates, category);
        model.addAttribute("filteredCabs", availableCabs);

        return "addBooking";
    }
    
    @GetMapping("/ownBookings")
    public String viewOwnBookings(Model model) {
        if (sessionData.getLoggedClient() == null) {
            return "redirect:/";
        }

        int clientID = sessionData.getLoggedClient().getClientID();
        List<BookingModel> myBookings = bookingRepository.getBookingsByClientID(clientID);

        List<CabModel> cabDetails = new ArrayList<>();
        for (BookingModel booking : myBookings) {
            CabModel cab = cabsRepository.getCab(booking.getCabID());
            if (cab != null) {
                cabDetails.add(cab);
            } else {
                cabDetails.add(new CabModel()); 
            }
        }


        model.addAttribute("sessionData", sessionData);
        model.addAttribute("myBookings", myBookings);
        model.addAttribute("cabDetails", cabDetails);

        return "myBookings";
    }
    
    @GetMapping("/allBookings")
    public String getAllBookings(Model model) {
        List<BookingModel> bookings = bookingRepository.getAllBookings();
        List<CabModel> cabDetails = new ArrayList<>();
        for (BookingModel booking : bookings) {
            CabModel cab = cabsRepository.getCab(booking.getCabID());
            if (cab != null) {
                cabDetails.add(cab);
            } else {
                cabDetails.add(new CabModel()); 
            }
        }
        
        model.addAttribute("bookings", bookings);
        model.addAttribute("cabDetails", cabDetails);
        return "bookings";
    }


    @GetMapping("/new")
    public String newBooking(@RequestParam String cabID, 
                             @RequestParam String cabNumber, 
                             @RequestParam String cabModel, 
                             @RequestParam String category, 
                             @RequestParam String seats, 
                             @RequestParam String driverName, 
                             @RequestParam String driverContact, 
                             @RequestParam String dates, 
                             @RequestParam String imageUrl,
                             Model model) {
        model.addAttribute("cabID", cabID);
        model.addAttribute("cabNumber", cabNumber);
        model.addAttribute("cabModel", cabModel);
        model.addAttribute("category", category);
        model.addAttribute("seats", seats);
        model.addAttribute("driverName", driverName);
        model.addAttribute("driverContact", driverContact);
        model.addAttribute("dates", dates);
        model.addAttribute("imageUrl", imageUrl);
        
        model.addAttribute("sessionData", sessionData);

        return "newBooking";  
    }



    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam int cabID, 
                                 @RequestParam String cabNumber, 
                                 @RequestParam String cabModel, 
                                 @RequestParam String category, 
                                 @RequestParam int seats, 
                                 @RequestParam String driverName, 
                                 @RequestParam String driverContact, 
                                 @RequestParam String dates, 
                                 @RequestParam String driverOption,
                                 Model model) {

        if (sessionData.getLoggedClient() == null) {
            return "redirect:/";  
        }
        
        TaxAndDiscountModel taxAndDiscount = taxAndDiscountRepository.getTaxAndDiscount(1);
        double taxRate = (taxAndDiscount != null) ? taxAndDiscount.getTaxPercentage(): 0.00;
        double discountRate = (taxAndDiscount != null) ? taxAndDiscount.getDiscountPercentage() : 0.00;

        CategoryModel categoryModel = cabCategoryRepository.getCategoryByName(category);
        if (categoryModel == null) {
            model.addAttribute("error", "Invalid category selected!");
            return "newBooking";
        }
        
        double categoryPrice = categoryModel.getPrice().doubleValue();
        double driverCost = categoryModel.getDriverCost().doubleValue();
        
        int totalDays = dates.split(",").length;  
        double totalCabAmount = totalDays * categoryPrice;
        double totalDriverAmount = 0; 

        if ("withDriver".equals(driverOption)) {
            totalDriverAmount = totalDays * driverCost; 
        }

        double totalAmount = totalCabAmount + totalDriverAmount;
        double totalTax = totalAmount * taxRate;
        double totalDiscount = totalAmount * discountRate;
        double totalAmountWithTaxAndDiscount = totalAmount + totalTax - totalDiscount;

        BookingModel booking = new BookingModel();
        booking.setClientID(sessionData.getLoggedClient().getClientID());
        booking.setCabID(cabID);
        booking.setDriverStatus(driverOption);
        booking.setBookedDates(dates);
        booking.setBookingDate(java.time.LocalDate.now().toString());
        booking.setBillAmount(totalAmountWithTaxAndDiscount);

        BookingModel savedBooking = bookingRepository.addBooking(booking);

        if (savedBooking != null) {
            model.addAttribute("cabAmount", totalCabAmount);
            model.addAttribute("driverAmount", totalDriverAmount);
            model.addAttribute("booking", savedBooking);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("taxRate", taxRate);
            model.addAttribute("discountRate", discountRate);
            model.addAttribute("totalTax", totalTax);
            model.addAttribute("totalDiscount", totalDiscount);
            return "bookingSuccess";
        } else {
            model.addAttribute("error", "Booking failed. Try again!");
            return "newBooking";
        }
    }

    @GetMapping("/delete/{bookingID}")
    public String deleteBooking(@PathVariable int bookingID, RedirectAttributes redirectAttributes) {
        BookingModel booking = bookingRepository.getBooking(bookingID);
        if (booking != null) {
            bookingRepository.deleteBooking(bookingID);
            redirectAttributes.addFlashAttribute("success", "Booking removed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to remove booking");
        }
        return "redirect:/booking/ownBookings";
    }
    
    @GetMapping("/adminDelete/{bookingID}")
    public String adminDeleteBookings(@PathVariable int bookingID, RedirectAttributes redirectAttributes) {
        BookingModel booking = bookingRepository.getBooking(bookingID);
        if (booking != null) {
            bookingRepository.deleteBooking(bookingID);
            redirectAttributes.addFlashAttribute("success", "Booking removed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to remove booking");
        }
        return "redirect:/booking/allBookings";
    }
}
