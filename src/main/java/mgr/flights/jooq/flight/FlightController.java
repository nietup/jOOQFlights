package mgr.flights.jooq.flight;

import mgr.flights.jooq.tables.Airport;
import mgr.flights.jooq.tables.City;
import mgr.flights.jooq.tables.daos.FlightDao;
import mgr.flights.jooq.tables.pojos.Flight;
import mgr.flights.jooq.tables.pojos.Passenger;
import mgr.flights.jooq.tables.records.FlightRecord;
import mgr.flights.jooq.tables.records.PassengerRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FlightController {

    private static final mgr.flights.jooq.tables.Flight FLIGHT = mgr.flights.jooq.tables.Flight.FLIGHT;
    private static final mgr.flights.jooq.tables.Passenger PASSENGER = mgr.flights.jooq.tables.Passenger.PASSENGER;
    private static final City CITY = City.CITY;
    private static final Airport AIRPORT = Airport.AIRPORT;

    private final FlightDao flightDao;
    private final DSLContext create;

    @Autowired
    public FlightController(FlightDao flightDao, DSLContext create) {
        this.flightDao = flightDao;
        this.create = create;
    }

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAll() {
        List<Flight> flights = flightDao.findAll();
        return ResponseEntity.ok().body(flights);
    }

    @GetMapping("/flights/{flightNo}")
    public ResponseEntity<Flight> getByFlightNo(@PathVariable String flightNo) {
        Flight flight = flightDao.fetchOneByFlightNo(flightNo);
        if (flight == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight with number: " + flightNo + " was not found");
        return ResponseEntity.ok().body(flight);
    }

    @GetMapping("/flights/{flightNo}/passengers")
    public ResponseEntity<List<Passenger>> getPassengersByFlightNo(@PathVariable String flightNo) {
        List<PassengerRecord> results = create
                .selectFrom(PASSENGER)
                .where(PASSENGER.FLIGHT_NO.eq(flightNo))
                .fetch();

        List<Passenger> passengers = results.stream()
                .map(p -> p.into(new Passenger()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(passengers);
    }

    @PostMapping("/flight-search")
    public ResponseEntity<List<Flight>> searchFlights(@RequestBody SearchRequest searchRequest) {
        List<FlightRecord> results = create
                .selectFrom(FLIGHT)
                .where(FLIGHT.SOURCE_IATA.in(create
                        .select(AIRPORT.IATA)
                        .from(AIRPORT)
                        .where(AIRPORT.CITY_ID.in(create
                                .select(CITY.CITY_ID)
                                .from(CITY)
                                .where(CITY.NAME.eq(searchRequest.getSourceCity()))))))
                .and(FLIGHT.DESTINATION_IATA.in(create
                        .select(AIRPORT.IATA)
                        .from(AIRPORT)
                        .where(AIRPORT.CITY_ID.in(create
                                .select(CITY.CITY_ID)
                                .from(CITY)
                                .where(CITY.NAME.eq(searchRequest.getDestinationCity()))))))
                .and(FLIGHT.START_TIME.greaterOrEqual(
                        searchRequest.getStartTime().minusHours(searchRequest.getTimeRange() / 2)))
                .and(FLIGHT.START_TIME.lessOrEqual(
                        searchRequest.getStartTime().plusHours(searchRequest.getTimeRange() / 2)))
                .fetch();

        List<Flight> flights = results.stream()
                .map(f -> f.into(new Flight()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(flights);
    }

    @PostMapping("/flights")
    public ResponseEntity<Flight> create(@RequestBody Flight flight) throws URISyntaxException {
        if (null != flightDao.fetchOneByFlightNo(flight.getFlightNo()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight with number: " + flight.getFlightNo() + " already exists");

        create.newRecord(FLIGHT, flight).store();

        Flight result = flightDao.fetchOneByFlightNo(flight.getFlightNo());
        return ResponseEntity.created(new URI("/api/flights/" + result.getFlightNo())).body(result);
    }

    @PutMapping("/flights/{flightNo}")
    public ResponseEntity<Flight> update(@PathVariable String flightNo, @RequestBody Flight flight) throws URISyntaxException {
        flight.setFlightNo(flightNo);
        if (null == flightDao.fetchOneByFlightNo(flight.getFlightNo()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight with number: " + flight.getFlightNo() + " doesn't exists");

        create.newRecord(FLIGHT, flight).update();

        Flight result = flightDao.fetchOneByFlightNo(flight.getFlightNo());
        return ResponseEntity.created(new URI("/api/flights/" + result.getFlightNo())).body(result);
    }

    @DeleteMapping("/flights/{flightNo}")
    public ResponseEntity<Void> delete(@PathVariable String flightNo) {
        create.deleteFrom(FLIGHT).where(FLIGHT.FLIGHT_NO.eq(flightNo)).execute();
        return ResponseEntity.noContent().build();
    }
}
