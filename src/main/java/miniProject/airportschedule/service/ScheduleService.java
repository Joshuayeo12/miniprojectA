package miniProject.airportschedule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleService {

    private static final String API_URL = "https://aviation-edge.com/v2/public/timetable";

    @Value("${flightapi.api_key:NOT_SET}")
    private String apiKey;

    /**
     * Fetch flight schedule from the external API.
     *
     * @param iataCode The IATA code of the airport.
     * @param type     The type of flights (departure or arrival).
     * @param status   The flight status (optional).
     * @return The raw JSON response as a string.
     */
    public String getFlightSchedule(String iataCode, String type, String status) {
        String url = API_URL + "?key=" + apiKey + "&iataCode=" + iataCode + "&type=" + type;
        if (status != null && !status.isEmpty()) {
            url += "&status=" + status;
        }

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        // Debugging the API response
        System.out.println("API Response: " + response);
        return response;
    }

    /**
     * Parse the JSON response from the API into a structured list of flight data.
     *
     * @param jsonResponse The raw JSON response as a string.
     * @return A list of maps representing flight data.
     */
    public List<Map<String, Object>> parseFlightData(String jsonResponse) {
        List<Map<String, Object>> flightData = new ArrayList<>();
        try {
            // Use Jackson ObjectMapper to parse the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            flightData = objectMapper.readValue(jsonResponse, new TypeReference<List<Map<String, Object>>>() {});

            // Format datetime fields
            for (Map<String, Object> flight : flightData) {
                Map<String, Object> departure = (Map<String, Object>) flight.get("departure");
                Map<String, Object> arrival = (Map<String, Object>) flight.get("arrival");

                if (departure != null) {
                    departure.put("formattedTime", formatDatetime((String) departure.get("scheduledTime")));
                }
                if (arrival != null) {
                    arrival.put("formattedTime", formatDatetime((String) arrival.get("scheduledTime")));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Debugging parsed flight data
        System.out.println("Parsed Flight Data: " + flightData);
        return flightData;
    }

    /**
     * Format datetime string to DD/MM/YYYY HH:mm.
     *
     * @param datetime The datetime string in the format YYYY-MM-DDTHH:mm:ss.SSS.
     * @return The formatted datetime string.
     */
    private String formatDatetime(String datetime) {
        if (datetime == null || datetime.isEmpty()) {
            return "";
        }
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(datetime, inputFormatter);
        return localDateTime.format(outputFormatter);
    }
}
