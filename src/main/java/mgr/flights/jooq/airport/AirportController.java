package mgr.flights.jooq.airport;

import mgr.flights.jooq.tables.daos.AirportDao;
import mgr.flights.jooq.tables.pojos.Airport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AirportController {

    private final AirportDao airportDao;

    @Autowired
    public AirportController(AirportDao airportDao) {
        this.airportDao = airportDao;
    }

    @GetMapping("/airports/{iata}")
    public ResponseEntity<Airport> getAirportByIata(@PathVariable String iata) {
        Airport airport = airportDao.fetchOneByIata(iata);
        if (airport == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Airport with iata: " + iata + " was not found.");
        return ResponseEntity.ok().body(airport);
    }
}
