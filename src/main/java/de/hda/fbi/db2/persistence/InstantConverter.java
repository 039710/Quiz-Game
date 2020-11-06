package de.hda.fbi.db2.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter class to convert a {@link Timestamp} to {@link Instant} and back.<br/>
 * Created by a.hofmann on 29.09.2016.
 */
@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(Instant instant) {
        if (instant == null) {
            return null;
        }
        return Timestamp.from(instant);
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toInstant();
    }
}
