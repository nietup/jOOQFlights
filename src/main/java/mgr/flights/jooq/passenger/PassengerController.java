package mgr.flights.jooq.passenger;

import mgr.flights.jooq.flight.FlightService;
import mgr.flights.jooq.tables.daos.PassengerDao;
import mgr.flights.jooq.tables.pojos.Passenger;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static mgr.flights.jooq.Tables.FLIGHT;
import static mgr.flights.jooq.Tables.PASSENGER;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerDao passengerDao;
    private final FlightService flightService;
    private final PassengerService passengerService;
    private final DSLContext create;
    private final PassengerFlightMapper passengerFlightMapper;

    @Autowired
    public PassengerController(PassengerDao passengerDao, FlightService flightService, PassengerService passengerService, DSLContext create, PassengerFlightMapper passengerFlightMapper) {
        this.passengerDao = passengerDao;
        this.flightService = flightService;
        this.passengerService = passengerService;
        this.create = create;
        this.passengerFlightMapper = passengerFlightMapper;
    }

    @GetMapping
    public ResponseEntity<List<Passenger>> getAll() {
        List<Passenger> passengers = passengerDao.findAll();
        return ResponseEntity.ok().body(passengers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> get(@PathVariable Integer id) {
        Passenger passenger = passengerDao.fetchOneByPassengerId(id);
        if (passenger == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger with id: " + id + " was not found");

        return ResponseEntity.ok().body(passenger);
    }

    @GetMapping("/{sub}/flights")
    public ResponseEntity<List<PassengerFlightDto>> getFlightsByPassegerSubject(@PathVariable String sub) {
        List<PassengerFlightDto> result = create
                .select(PASSENGER.FIRST_NAME, PASSENGER.LAST_NAME, FLIGHT.FLIGHT_NO, FLIGHT.START_TIME, FLIGHT.LANDING_TIME, FLIGHT.AIRCRAFT_ID, FLIGHT.SOURCE_IATA, FLIGHT.DESTINATION_IATA)
                .from(PASSENGER, FLIGHT)
                .where(PASSENGER.FLIGHT_NO.eq(FLIGHT.FLIGHT_NO)
                        .and(PASSENGER.SUB.eq(sub)))
                .fetch()
                .stream()
                .map(passengerFlightMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<Passenger> create(@RequestBody Passenger passenger) {
        if (passenger.getPassengerId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New passenger cannot have an id");

        if (!flightService.hasFreeSeats(passenger.getFlightNo()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chosen flights has no seats available");

        passengerDao.insert(passenger);
        Passenger result = passengerDao.fetchOneByPassportNo(passenger.getPassportNo());

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passenger> update(@PathVariable Integer id, @RequestBody Passenger passenger) {
        passenger.setPassengerId(id);
        if (passenger.getPassengerId() == null || passengerDao.fetchOneByPassengerId(passenger.getPassengerId()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id: " + id);
        }
        if (!passengerService.changedFlights(passenger) || flightService.hasFreeSeats(passenger.getFlightNo())) {
            passengerDao.update(passenger);
            Passenger result = passengerDao.fetchOneByPassengerId(id);
            return ResponseEntity.ok().body(result);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chosen flights has no seats available");
        }
    }

    @DeleteMapping("/{passengerId}")
    public ResponseEntity<Void> delete(@PathVariable Integer passengerId) {
        passengerDao.deleteById(passengerId);
        return ResponseEntity.noContent().build();
    }
}
