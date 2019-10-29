package mgr.flights.jooq.passenger;

import org.jooq.Record8;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class PassengerFlightMapper {
    PassengerFlightDto toDto(Record8<String, String, String, OffsetDateTime, OffsetDateTime, Integer, String, String> record) {
        return new PassengerFlightDto(
                (String) record.get(0),
                (String) record.get(1),
                (String) record.get(2),
                (OffsetDateTime) record.get(3),
                (OffsetDateTime) record.get(4),
                (Integer) record.get(5),
                (String) record.get(6),
                (String) record.get(7)
        );
    }
}
