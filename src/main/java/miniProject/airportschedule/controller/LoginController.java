package miniProject.airportschedule.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import miniProject.airportschedule.model.LoginModel;
import miniProject.airportschedule.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginModel", new LoginModel());
        return "/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @Valid @ModelAttribute("loginModel") LoginModel loginModel,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login"; // Reload the page if validation fails
        }

        String storedPassword = accountService.findPasswordByUsername(loginModel.getEmail());
        if (storedPassword == null || !storedPassword.equals(loginModel.getPassword())) {
            // Set error message if email or password is incorrect
            model.addAttribute("error", "Invalid email or password. Please try again.");
            return "login"; // Reload the login page with the error message
        }

        // Set session attribute and redirect to airport info
        session.setAttribute("userEmail", loginModel.getEmail());
        return "redirect:/airportinfo/" + loginModel.getEmail();
    }

}