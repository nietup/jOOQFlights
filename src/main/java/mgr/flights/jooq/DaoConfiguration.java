package mgr.flights.jooq;

import mgr.flights.jooq.tables.daos.*;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfiguration {

    private final DSLContext create;

    @Autowired
    public DaoConfiguration(DSLContext create) {
        this.create = create;
    }

    @Bean
    public CityDao cityDao() {
        return new CityDao(create.configuration());
    }

    @Bean
    public AircraftDao aircraftDao() {
        return new AircraftDao(create.configuration());
    }

    @Bean
    public AirportDao airportDao() {
        return new AirportDao(create.configuration());
    }

    @Bean
    public PassengerDao passengerDao() {
        return new PassengerDao(create.configuration());
    }

    @Bean
    public FlightDao flightDao() {
        return new FlightDao(create.configuration());
    }
}
