package mgr.flights.jooq.passenger;

import java.time.OffsetDateTime;

public class PassengerFlightDto {
    private String passportNo;
    private String firstName;
    private String lastName;
    private String flightNo;
    private OffsetDateTime startTime;
    private OffsetDateTime landingTime;
    private Integer aircraftId;
    private String sourceIata;
    private String destinationIata;

    public PassengerFlightDto(String passportNo, String firstName, String lastName, String flightNo, OffsetDateTime startTime, OffsetDateTime landingTime, Integer aircraftId, String sourceIata, String destinationIata) {
        this.passportNo = passportNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.flightNo = flightNo;
        this.startTime = startTime;
        this.landingTime = landingTime;
        this.aircraftId = aircraftId;
        this.sourceIata = sourceIata;
        this.destinationIata = destinationIata;
    }

    public PassengerFlightDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public OffsetDateTime getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(OffsetDateTime landingTime) {
        this.landingTime = landingTime;
    }

    public Integer getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(Integer aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getSourceIata() {
        return sourceIata;
    }

    public void setSourceIata(String sourceIata) {
        this.sourceIata = sourceIata;
    }

    public String getDestinationIata() {
        return destinationIata;
    }

    public void setDestinationIata(String destinationIata) {
        this.destinationIata = destinationIata;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }
}
