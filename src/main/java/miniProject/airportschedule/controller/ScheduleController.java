package miniProject.airportschedule.controller;

import miniProject.airportschedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/flightschedule")
    public String getFlightSchedule(
            @RequestParam String iataCode,
            @RequestParam String type,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // If "all" is selected for status, treat it as null to fetch all flights
        if ("all".equalsIgnoreCase(status)) {
            status = null;
        }

        // Fetch flight data using the ScheduleService
        String jsonResponse = scheduleService.getFlightSchedule(iataCode, type, status);
        List<Map<String, Object>> flightData = scheduleService.parseFlightData(jsonResponse);

        // Pagination logic
        int totalItems = flightData.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<Map<String, Object>> paginatedData = flightData.subList(startIndex, endIndex);

        // Add data and pagination info to the model
        model.addAttribute("flightData", paginatedData);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("iataCode", iataCode);
        model.addAttribute("type", type);
        model.addAttribute("status", status != null ? status : "All");

        return "flightschedule";
    }
}


