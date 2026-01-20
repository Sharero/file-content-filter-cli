package org.shift;

import org.junit.jupiter.api.Test;
import org.shift.stats.FloatStatisticCollector;
import org.shift.stats.StatisticsType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloatStatisticCollectorTest {

    @Test
    void testShortStatisticsWithZeroElements() {
        FloatStatisticCollector floatStatisticCollector =
                new FloatStatisticCollector(StatisticsType.SHORT);

        assertEquals("Count: 0", floatStatisticCollector.getStatistics());
    }

    @Test
    void testShortStatisticsWithElements() {
        FloatStatisticCollector floatStatisticCollector =
                new FloatStatisticCollector(StatisticsType.SHORT);

        floatStatisticCollector.addValue("5.0");
        floatStatisticCollector.addValue("5.0E1");
        floatStatisticCollector.addValue("50E-1");

        assertEquals("Count: 3", floatStatisticCollector.getStatistics());
    }

    @Test
    void testShortStatisticsWithInvalidElements() {
        FloatStatisticCollector floatStatisticCollector =
                new FloatStatisticCollector(StatisticsType.SHORT);

        floatStatisticCollector.addValue("5.0");
        floatStatisticCollector.addValue("5.0E1");
        floatStatisticCollector.addValue("50E-1");
        floatStatisticCollector.addValue("abc");

        assertEquals("Count: 3", floatStatisticCollector.getStatistics());
    }

    @Test
    void testFullStatisticsWithZeroElements() {
        FloatStatisticCollector floatStatisticCollector =
                new FloatStatisticCollector(StatisticsType.FULL);

        assertEquals("Count: 0", floatStatisticCollector.getStatistics());
    }

    @Test
    void testFullStatisticsWithElements() {
        FloatStatisticCollector floatStatisticCollector =
                new FloatStatisticCollector(StatisticsType.FULL);

        floatStatisticCollector.addValue("5.0");
        floatStatisticCollector.addValue("5.1E1");
        floatStatisticCollector.addValue("50E-1");

        assertEquals("Count: 3, Min: 5.0, Max: 51.0, Sum: 61.0, Avg: 20.333333333333332",
                floatStatisticCollector.getStatistics());
    }

    @Test
    void testFullStatisticsWithInvalidElements() {
        FloatStatisticCollector floatStatisticCollector =
                new FloatStatisticCollector(StatisticsType.FULL);

        floatStatisticCollector.addValue("5.0");
        floatStatisticCollector.addValue("5.1E1");
        floatStatisticCollector.addValue("50E-1");
        floatStatisticCollector.addValue("abc");

        assertEquals("Count: 3, Min: 5.0, Max: 51.0, Sum: 61.0, Avg: 20.333333333333332",
                floatStatisticCollector.getStatistics());
    }
}
