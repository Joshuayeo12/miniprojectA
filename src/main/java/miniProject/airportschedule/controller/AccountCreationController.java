package miniProject.airportschedule.controller;

import jakarta.validation.Valid;
import miniProject.airportschedule.model.AccountCreationModel;
import miniProject.airportschedule.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountCreationController {

    private final AccountService accountService;

    @Autowired
    public AccountCreationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account-creation")
    public String showAccountCreationPage(Model model) {
        model.addAttribute("accountCreationModel", new AccountCreationModel());
        return "accountcreation";
    }

    @PostMapping("/account-creation")
    public String processAccountCreation(
            @Valid @ModelAttribute("accountCreationModel") AccountCreationModel accountCreationModel,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "accountcreation"; // For validation errors
        }

        try {
            accountService.createAccount(accountCreationModel.getEmail(), accountCreationModel.getPassword());
            model.addAttribute("success", "Account successfully created! You can now log in.");
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "accountcreation"; // Return the form with error message
        }

        return "redirect:/login"; // Redirect to login page after successful account creation
    }
}