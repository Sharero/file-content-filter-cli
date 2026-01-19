package org.shift;

public class StringStatisticCollector implements StatisticsCollector {

    private final StatisticsType statisticsType;

    private int elementsCount = 0;

    private int minStringLength = Integer.MAX_VALUE;

    private int maxStringLength = 0;

    public StringStatisticCollector(StatisticsType statisticsType) {
        this.statisticsType = statisticsType;
    }

    @Override
    public void addValue(String value) {
        elementsCount++;
        if (statisticsType == StatisticsType.FULL) {
            int valueLength = value.length();
            minStringLength = Math.min(minStringLength, valueLength);
            maxStringLength = Math.max(maxStringLength, valueLength);
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
                sb.append(", MinLength: ").append(minStringLength);
                sb.append(", MaxLength: ").append(maxStringLength);
            }
            return sb.toString();
        }
    }
}
