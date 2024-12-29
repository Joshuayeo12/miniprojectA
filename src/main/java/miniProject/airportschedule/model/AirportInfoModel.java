package miniProject.airportschedule.model;

import jakarta.validation.constraints.NotEmpty;

public class AirportInfoModel {
    @NotEmpty(message = "Please input an airport in IATA format")
    private String IataCode;

    @NotEmpty(message = "Please select an option")
    private String FlightType;

    @NotEmpty(message = "Please select a status")
    private String FlightStatus;

    public String getIataCode() {
        return IataCode;
    }

    public void setIataCode(String iataCode) {
        IataCode = iataCode;
    }

    public String getFlightType() {
        return FlightType;
    }

    public void setFlightType(String flightType) {
        FlightType = flightType;
    }

    public String getFlightStatus() {
        return FlightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        FlightStatus = flightStatus;
    }

    @Override
    public String toString() {
        return "AirportInfoModel{" +
                "IataCode='" + IataCode + '\'' +
                ", FlightType='" + FlightType + '\'' +
                ", FlightStatus='" + FlightStatus + '\'' +
                '}';
    }
}
