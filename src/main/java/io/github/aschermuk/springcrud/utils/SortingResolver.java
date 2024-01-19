package io.github.aschermuk.springcrud.utils;

import static org.springframework.data.domain.Sort.Direction.fromString;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

public class SortingResolver {
    public static Sort resolveSorting(String sorts) {
        try {
            List<Order> orders = new ArrayList<>();
            if (sorts != null) {
                parseSorts(sorts, orders);
            }
            return Sort.by(orders);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing sort parameter: " + sorts, e);
        }
    }

    private static void parseSorts(String sorts, List<Order> orders) {
        String[] splitSorts = sorts.split(";");
        for (String sort : splitSorts) {
            parseSort(sort, orders);
        }
    }

    private static void parseSort(String sort, List<Order> orders) {
        String[] splitSort = sort.split(",");
        orders.add(resolveOrder(splitSort));
    }

    private static Order resolveOrder(String[] splitSort) {
        return new Order(resolveDirection(splitSort[1]), splitSort[0]);
    }

    private static Direction resolveDirection(String s) {
        return fromString(s.toUpperCase());
    }
}
