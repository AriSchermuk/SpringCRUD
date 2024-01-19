package io.github.arischermuk.springcrud.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.YearMonth;

public class YearMonthJPAConverterTest {

    private final YearMonthJPAConverter converter = new YearMonthJPAConverter();

    @Test
    public void testConvertToDatabaseColumn() {
        // Arrange
        YearMonth yearMonth = YearMonth.of(2022, 3);

        // Act
        Date result = converter.convertToDatabaseColumn(yearMonth);

        // Assert
        assertEquals(Date.valueOf("2022-03-01"), result);
    }

    @Test
    public void testConvertToEntityAttribute() {
        // Arrange
        Date date = Date.valueOf("2022-03-04");

        // Act
        YearMonth result = converter.convertToEntityAttribute(date);

        // Assert
        assertEquals(YearMonth.of(2022, 3), result);
    }
}
