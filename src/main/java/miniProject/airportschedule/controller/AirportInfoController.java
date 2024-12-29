package miniProject.airportschedule.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import miniProject.airportschedule.model.AirportInfoModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AirportInfoController {

    @GetMapping("/airportinfo/{email}")
    public String showAirportInfoPage(
            @PathVariable String email,
            Model model,
            HttpSession session) {

        String loggedInEmail = (String) session.getAttribute("userEmail");
        if (loggedInEmail == null || !loggedInEmail.equals(email)) {
            return "redirect:/login"; // Redirect to login if session is invalid
        }

        model.addAttribute("email", loggedInEmail);
        model.addAttribute("airportInfoModel", new AirportInfoModel());
        return "AirportInfo"; // Render AirportInfo.html
    }

    @PostMapping("/flightschedule")
    public String processAirportInfo(
            @Valid @ModelAttribute("airportInfoModel") AirportInfoModel airportInfoModel,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "AirportInfo";
        }

        // Set status to null if "all" is selected, so it fetches all flights
        String status = airportInfoModel.getFlightStatus();
        if ("all".equalsIgnoreCase(status)) {
            status = null;
        }

        // Redirect to the flight schedule page with query parameters
        return "redirect:/flightschedule?iataCode=" + airportInfoModel.getIataCode() +
                "&type=" + airportInfoModel.getFlightType() +
                "&status=" + (status != null ? status : "");
    }


}