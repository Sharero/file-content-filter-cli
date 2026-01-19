package org.shift;

public class IntegerStatisticCollector implements StatisticsCollector {

    private final StatisticsType statisticsType;

    private int elementsCount = 0;

    private long elementsSum = 0;

    private long minElement = Integer.MAX_VALUE;

    private long maxElement = Integer.MIN_VALUE;

    public IntegerStatisticCollector(StatisticsType statisticsType) {
        this.statisticsType = statisticsType;
    }

    @Override
    public void addValue(String value) {
        try {
            long num = Long.parseLong(value.trim());
            elementsCount++;
            if (statisticsType == StatisticsType.FULL) {
                minElement = Math.min(minElement, num);
                maxElement = Math.max(maxElement, num);
                elementsSum += num;
            }
        } catch (NumberFormatException e) {
            System.err.println("Skipping invalid integer value: " + value);
        }
    }

    @Override
    public String getStatistics() {
        if (statisticsType == StatisticsType.BRIEF) {
            return "Count: " + elementsCount;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Count: ").append(elementsCount);
            if (elementsCount > 0) {
                sb.append(", Min: ").append(minElement);
                sb.append(", Max: ").append(maxElement);
                sb.append(", Sum: ").append(elementsSum);
                sb.append(", Avg: ").append((double) elementsSum / elementsCount);
            }
            return sb.toString();
        }
    }
}
