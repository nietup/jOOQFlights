package mgr.flights.jooq.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
class SearchRequest {
    @NonNull
    private String sourceCity;
    @NonNull
    private String destinationCity;
    @NonNull
    private OffsetDateTime startTime;
    private Integer timeRange = 10;
}
