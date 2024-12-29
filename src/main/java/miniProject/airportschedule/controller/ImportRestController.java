package miniProject.airportschedule.controller;


import jakarta.servlet.http.HttpServletResponse;
import miniProject.airportschedule.service.ScheduleService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImportRestController {

    private final ScheduleService scheduleService;

    public ImportRestController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/flights")
    public List<Map<String, Object>> getFlights(@RequestParam String iataCode, @RequestParam String type) {
        return scheduleService.parseFlightData(scheduleService.getFlightSchedule(iataCode, type, null));
    }

    @GetMapping("/documentation/csv")
    public void getApiDocumentationCsv(HttpServletResponse response) {
        try {
            // Set the content type and headers for CSV
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"api_documentation.csv\"");

            // Write CSV headers and content
            PrintWriter writer = response.getWriter();
            writer.println("Method,URL,Description,Parameters,Sample Response");


            List<Map<String, Object>> endpoints = List.of(
                    Map.of(
                            "method", "GET",
                            "url", "/api/flights",
                            "description", "Fetch flight schedules based on IATA code and type.",
                            "parameters", "iataCode(String), type(String), status(String - optional)",
                            "sampleResponse", "\"{flightNumber:ABC123, departure:JFK, arrival:LAX}\""
                    )
            );

            // Write each endpoint as a CSV row
            for (Map<String, Object> endpoint : endpoints) {
                writer.printf(
                        "%s,%s,%s,%s,%s%n",
                        endpoint.get("method"),
                        endpoint.get("url"),
                        endpoint.get("description"),
                        endpoint.get("parameters"),
                        endpoint.get("sampleResponse")
                );
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}