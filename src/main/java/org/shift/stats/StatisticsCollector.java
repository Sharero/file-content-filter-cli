package org.shift.stats;

public interface StatisticsCollector {
    void addValue(String value);

    String getStatistics();
}
