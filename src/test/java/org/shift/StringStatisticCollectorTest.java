package org.shift;

import org.junit.jupiter.api.Test;
import org.shift.stats.StatisticsType;
import org.shift.stats.StringStatisticCollector;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringStatisticCollectorTest {

    @Test
    void testShortStatisticsWithZeroElements() {
        StringStatisticCollector stringStatisticCollector =
                new StringStatisticCollector(StatisticsType.SHORT);

        assertEquals("Count: 0", stringStatisticCollector.getStatistics());
    }

    @Test
    void testShortStatisticsWithElements() {
        StringStatisticCollector stringStatisticCollector =
                new StringStatisticCollector(StatisticsType.SHORT);

        stringStatisticCollector.addValue("My dear");
        stringStatisticCollector.addValue("Home");

        assertEquals("Count: 2", stringStatisticCollector.getStatistics());
    }

    @Test
    void testFullStatisticsWithZeroElements() {
        StringStatisticCollector stringStatisticCollector =
                new StringStatisticCollector(StatisticsType.FULL);

        assertEquals("Count: 0", stringStatisticCollector.getStatistics());
    }

    @Test
    void testFullStatisticsWithElements() {
        StringStatisticCollector stringStatisticCollector =
                new StringStatisticCollector(StatisticsType.FULL);

        stringStatisticCollector.addValue("My dear");
        stringStatisticCollector.addValue("Home");

        assertEquals("Count: 2, MinLength: 4, MaxLength: 7",
                stringStatisticCollector.getStatistics());
    }
}