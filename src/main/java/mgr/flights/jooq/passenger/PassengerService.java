package mgr.flights.jooq.passenger;

import mgr.flights.jooq.tables.daos.PassengerDao;
import mgr.flights.jooq.tables.pojos.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    private final PassengerDao passengerDao;

    @Autowired
    public PassengerService(PassengerDao passengerDao) {
        this.passengerDao = passengerDao;
    }

    Boolean changedFlights(Passenger newPassenger) {
        Passenger savedPassenger = passengerDao.fetchOneByPassengerId(newPassenger.getPassengerId());
        return !savedPassenger.getFlightNo().equalsIgnoreCase(newPassenger.getFlightNo());
    }
}
