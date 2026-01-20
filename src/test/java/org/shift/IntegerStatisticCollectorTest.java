package org.shift;

import org.junit.jupiter.api.Test;
import org.shift.stats.IntegerStatisticCollector;
import org.shift.stats.StatisticsType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegerStatisticCollectorTest {

    @Test
    void testShortStatisticsWithZeroElements() {
        IntegerStatisticCollector integerStatisticCollector =
                new IntegerStatisticCollector(StatisticsType.SHORT);

        assertEquals("Count: 0", integerStatisticCollector.getStatistics());
    }

    @Test
    void testShortStatisticsWithElements() {
        IntegerStatisticCollector integerStatisticCollector =
                new IntegerStatisticCollector(StatisticsType.SHORT);

        integerStatisticCollector.addValue("5");
        integerStatisticCollector.addValue("51");

        assertEquals("Count: 2", integerStatisticCollector.getStatistics());
    }

    @Test
    void testShortStatisticsWithInvalidElements() {
        IntegerStatisticCollector integerStatisticCollector =
                new IntegerStatisticCollector(StatisticsType.SHORT);

        integerStatisticCollector.addValue("5");
        integerStatisticCollector.addValue("abc");
        integerStatisticCollector.addValue("51");

        assertEquals("Count: 2", integerStatisticCollector.getStatistics());
    }

    @Test
    void testFullStatisticsWithZeroElements() {
        IntegerStatisticCollector integerStatisticCollector =
                new IntegerStatisticCollector(StatisticsType.FULL);

        assertEquals("Count: 0", integerStatisticCollector.getStatistics());
    }

    @Test
    void testFullStatisticsWithElements() {
        IntegerStatisticCollector integerStatisticCollector =
                new IntegerStatisticCollector(StatisticsType.FULL);

        integerStatisticCollector.addValue("5");
        integerStatisticCollector.addValue("51");

        assertEquals("Count: 2, Min: 5, Max: 51, Sum: 56, Avg: 28.0",
                integerStatisticCollector.getStatistics());
    }

    @Test
    void testFullStatisticsWithInvalidElements() {
        IntegerStatisticCollector integerStatisticCollector =
                new IntegerStatisticCollector(StatisticsType.FULL);

        integerStatisticCollector.addValue("5");
        integerStatisticCollector.addValue("abc");
        integerStatisticCollector.addValue("51");

        assertEquals("Count: 2, Min: 5, Max: 51, Sum: 56, Avg: 28.0",
                integerStatisticCollector.getStatistics());
    }
}
