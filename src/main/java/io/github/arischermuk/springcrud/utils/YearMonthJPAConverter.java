package io.github.arischermuk.springcrud.utils;

import static java.time.ZoneId.systemDefault;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.sql.Date;

@Converter(autoApply = true)
public class YearMonthJPAConverter implements AttributeConverter<YearMonth, Date> {
    @Override
    public Date convertToDatabaseColumn(YearMonth yearMonth) {
        return Date.valueOf(yearMonth.atDay(1));
    }

    @Override
    public YearMonth convertToEntityAttribute(Date date) {
        return YearMonth.from(getLocalDate(date));
    }

    private LocalDate getLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
            .atZone(systemDefault())
            .toLocalDate();
    }
}