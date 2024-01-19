package io.github.aschermuk.springcrud.utils;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

public class SortingResolverTest {

    @Test
    public void testResolveSortingWithValidSorts() {
        // Arrange
        String sorts = "field1,asc;field2,desc";

        // Act
        Sort result = SortingResolver.resolveSorting(sorts);

        // Assert
        assertNotNull(result);
        Order field1 = result.getOrderFor("field1");
        assertEquals(0, getOrderList(result).indexOf(field1));
        assertEquals(ASC, requireNonNull(field1).getDirection());
        Order field2 = result.getOrderFor("field2");
        assertEquals(1, getOrderList(result).indexOf(field2));
        assertEquals(DESC, requireNonNull(field2).getDirection());
    }

    @Test
    public void testResolveSortingWithNullSorts() {
        // Act
        Sort result = SortingResolver.resolveSorting(null);

        // Assert
        assertNotNull(result);
        assertEquals(0, getOrderList(result).size());
    }

    @Test
    public void testResolveSortingWithEmptySorts() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> SortingResolver.resolveSorting(""));
    }

    @Test
    public void testResolveSortingWithInvalidSortFormat() {
        // Arrange
        String invalidSorts = "invalidSortFormat";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> SortingResolver.resolveSorting(invalidSorts));
    }

    @Test
    public void testResolveSortingWithInvalidDirection() {
        // Arrange
        String invalidSorts = "field1,invalidDirection";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> SortingResolver.resolveSorting(invalidSorts));
    }

    @Test
    public void testResolveSortingWithNullDirection() {
        // Arrange
        String sorts = "field1,null";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> SortingResolver.resolveSorting(sorts));
    }

    private static List<Order> getOrderList(Sort result) {
        return result.stream().toList();
    }
}
