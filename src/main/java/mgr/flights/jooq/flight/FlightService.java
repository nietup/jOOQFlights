package mgr.flights.jooq.flight;

import mgr.flights.jooq.tables.Aircraft;
import mgr.flights.jooq.tables.Flight;
import mgr.flights.jooq.tables.Passenger;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    private static final Passenger PASSENGER = Passenger.PASSENGER;
    private static final Aircraft AIRCRAFT = Aircraft.AIRCRAFT;
    private static final Flight FLIGHT = Flight.FLIGHT;

    private final DSLContext create;

    @Autowired
    public FlightService(DSLContext create) {
        this.create = create;
    }

    public Boolean hasFreeSeats(String flightNo) {
        int passengerCount = (int) create
                .selectCount()
                .from(PASSENGER)
                .where(PASSENGER.FLIGHT_NO.eq(flightNo))
                .fetch()
                .getValue(0, 0);

        int seats = (int) create
                .select(AIRCRAFT.SEATS)
                .from(AIRCRAFT)
                .where(AIRCRAFT.AIRCRAFT_ID.eq(create.select(FLIGHT.AIRCRAFT_ID)
                        .from(FLIGHT)
                        .where(FLIGHT.FLIGHT_NO.eq(flightNo))))
                .fetch()
                .getValue(0, 0);

        return seats > passengerCount;
    }
}
