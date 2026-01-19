package org.shift;

public interface StatisticsCollector {
    void addValue(String value);

    String getStatistics();
}
