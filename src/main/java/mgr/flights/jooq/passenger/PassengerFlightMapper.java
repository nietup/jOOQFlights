package mgr.flights.jooq.passenger;

import org.jooq.Record9;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class PassengerFlightMapper {
    PassengerFlightDto toDto(Record9<String, String, String, String, OffsetDateTime, OffsetDateTime, Integer, String, String> record) {
        return new PassengerFlightDto(
                (String) record.get(0),
                (String) record.get(1),
                (String) record.get(2),
                (String) record.get(3),
                (OffsetDateTime) record.get(4),
                (OffsetDateTime) record.get(5),
                (Integer) record.get(6),
                (String) record.get(7),
                (String) record.get(8)
        );
    }
}
