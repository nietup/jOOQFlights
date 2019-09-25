package mgr.flights.jooq.city;

import mgr.flights.jooq.tables.daos.CityDao;
import mgr.flights.jooq.tables.pojos.Airport;
import mgr.flights.jooq.tables.pojos.City;
import mgr.flights.jooq.tables.records.AirportRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CityController {

    private static final mgr.flights.jooq.tables.City CITY = mgr.flights.jooq.tables.City.CITY;
    private static final mgr.flights.jooq.tables.Airport AIRPORT = mgr.flights.jooq.tables.Airport.AIRPORT;

    private final CityDao cityDao;
    private final DSLContext create;

    @Autowired
    public CityController(CityDao cityDao, DSLContext create) {
        this.cityDao = cityDao;
        this.create = create;
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityDao.findAll();
        return ResponseEntity.ok().body(cities);
    }

    @GetMapping("/cities/{cityName}")
    public ResponseEntity<City> getCityByName(@PathVariable String cityName) {
        City city = cityDao.fetchOneByName(cityName);
        if (city == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City: " + cityName + " was not found.");
        return ResponseEntity.ok().body(city);
    }

    @GetMapping("/cities/{cityName}/airports")
    public ResponseEntity<List<Airport>> getAirportsByCityName(@PathVariable String cityName) {
        List<AirportRecord> airportRecords = create
                .select()
                .from(AIRPORT)
                .where(AIRPORT.CITY_ID.eq(create
                        .select(CITY.CITY_ID)
                        .from(CITY)
                        .where(CITY.NAME.eq(cityName))))
                .fetchInto(AIRPORT);

        List<Airport> airports = airportRecords.stream()
                .map(a -> a.into(new Airport()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(airports);
    }
}
