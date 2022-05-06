package io.inspect.koreanstockinspector.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class LocalDateTimeConverter implements DynamoDBTypeConverter<Date, LocalDateTime> {
    @Override
    public Date convert(LocalDateTime ldt) {
        // LocalDateTime -> Instant -> Date
        return Date.from(ldt.toInstant(ZoneOffset.UTC));
    }

    @Override
    public LocalDateTime unconvert(Date date) {
        // Date -> Instant -> LocalDateTime
        return date.toInstant().atZone(ZoneId.of("Etc/UTC")).toLocalDateTime();
    }
}
