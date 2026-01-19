package org.shift.stats;

public class FloatStatisticCollector implements StatisticsCollector {

    private final StatisticsType statisticsType;

    private int elementsCount = 0;

    private double elementsSum = 0;

    private double minElement = Double.MAX_VALUE;

    private double maxElement = Double.MIN_VALUE;

    public FloatStatisticCollector(StatisticsType statisticsType) {
        this.statisticsType = statisticsType;
    }

    @Override
    public void addValue(String value) {
        try {
            double num = Double.parseDouble(value.trim());
            elementsCount++;
            if (statisticsType == StatisticsType.FULL) {
                minElement = Math.min(minElement, num);
                maxElement = Math.max(maxElement, num);
                elementsSum += num;
            }
        } catch (NumberFormatException e) {
            System.err.printf(
                    "Skipping invalid float value: '%s'%n",
                    value
            );
        }
    }

    @Override
    public String getStatistics() {
        if (statisticsType == StatisticsType.SHORT) {
            return "Count: " + elementsCount;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Count: ").append(elementsCount);
        if (elementsCount > 0) {
            sb.append(", Min: ").append(minElement);
            sb.append(", Max: ").append(maxElement);
            sb.append(", Sum: ").append(elementsSum);
            sb.append(", Avg: ").append(elementsSum / elementsCount);
        }

        return sb.toString();
    }
}
