package org.shift.core;

import org.shift.cli.CommandLineArguments;
import org.shift.stats.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class OutputManager {
    private Path outputDirectoryName;

    private StatisticsType statisticsType;

    private String outputFileNamePrefix;

    private boolean isAppendToExistingFiles;

    private Map<DataType, BufferedWriter> writers = new EnumMap<>(DataType.class);

    private EnumSet<DataType> disabledTypes = EnumSet.noneOf(DataType.class);

    private Map<DataType, StatisticsCollector> statistics = new EnumMap<>(DataType.class);

    public OutputManager(CommandLineArguments commandLineArguments) {
        this.isAppendToExistingFiles = commandLineArguments.getIsAppendToExistingFiles();
        this.statisticsType = commandLineArguments.getStatisticsType();
        this.outputDirectoryName = commandLineArguments.getOutputDirectoryName() == null ?
                Paths.get(".") : commandLineArguments.getOutputDirectoryName();
        this.outputFileNamePrefix = commandLineArguments.getOutputFileNamePrefix();
    }

    public Path getOutputDirectoryName() {
        return outputDirectoryName;
    }

    public void setOutputDirectoryName(Path outputDirectoryName) {
        this.outputDirectoryName = outputDirectoryName;
    }

    public StatisticsType getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(StatisticsType statisticsType) {
        this.statisticsType = statisticsType;
    }

    public String getOutputFileNamePrefix() {
        return outputFileNamePrefix;
    }

    public void setOutputFileNamePrefix(String outputFileNamePrefix) {
        this.outputFileNamePrefix = outputFileNamePrefix;
    }

    public boolean isAppendToExistingFiles() {
        return isAppendToExistingFiles;
    }

    public void setAppendToExistingFiles(boolean appendToExistingFiles) {
        this.isAppendToExistingFiles = appendToExistingFiles;
    }

    public Map<DataType, BufferedWriter> getWriters() {
        return writers;
    }

    public void setWriters(Map<DataType, BufferedWriter> writers) {
        this.writers = writers;
    }

    public EnumSet<DataType> getDisabledTypes() {
        return disabledTypes;
    }

    public void setDisabledTypes(EnumSet<DataType> disabledTypes) {
        this.disabledTypes = disabledTypes;
    }

    public Map<DataType, StatisticsCollector> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<DataType, StatisticsCollector> statistics) {
        this.statistics = statistics;
    }


    private String getFileNameForDataType(DataType dataType) {
        return switch (dataType) {
            case INTEGER -> outputFileNamePrefix + "integers.txt";
            case FLOAT -> outputFileNamePrefix + "floats.txt";
            case STRING -> outputFileNamePrefix + "strings.txt";
        };
    }

    private BufferedWriter createWriter(DataType dataType) throws IOException {
        try {
            Files.createDirectories(outputDirectoryName);
        } catch (IOException e) {
            throw new IOException(
                    "Cannot create output directory: " + outputDirectoryName, e
            );
        }

        Path path = outputDirectoryName.resolve(getFileNameForDataType(dataType));

        OpenOption[] fileMode = isAppendToExistingFiles
                ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.APPEND}
                : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING};

        return Files.newBufferedWriter(path, java.nio.charset.StandardCharsets.UTF_8, fileMode);
    }

    private void closeWriterByError(DataType dataType) {
        BufferedWriter writer = writers.remove(dataType);
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.printf(
                        "Failed to close writer for %s: %s%n",
                        dataType, e.getMessage()
                );
            }
        }
    }

    public void writeLine(DataType dataType, String line) throws IOException {
        if (disabledTypes.contains(dataType)) {
            return;
        }

        try {
            BufferedWriter writer = writers.get(dataType);

            if (writer == null) {
                writer = createWriter(dataType);
                writers.put(dataType, writer);
            }

            writer.write(line);
            writer.newLine();

            if (statisticsType != null) {
                StatisticsCollector collector = statistics.computeIfAbsent(dataType, dt -> {
                    return switch (dt) {
                        case INTEGER -> new IntegerStatisticCollector(statisticsType);
                        case FLOAT -> new FloatStatisticCollector(statisticsType);
                        case STRING -> new StringStatisticCollector(statisticsType);
                    };
                });

                collector.addValue(line);
            }
        } catch (IOException e) {
            System.err.printf(
                    "Failed to write %s data to file (%s). This data type will be skipped.%n",
                    dataType, e.getMessage()
            );

            closeWriterByError(dataType);
            disabledTypes.add(dataType);

            if (disabledTypes.size() == DataType.values().length) {
                System.err.println("No output files available");
                System.exit(1);
            }
        }
    }

    public void printStatistics() {
        System.out.println("=== Statistics ===");
        for (DataType dt : DataType.values()) {
            StatisticsCollector collector = statistics.get(dt);
            if (collector != null) {
                System.out.printf("%s: %s%n", dt, collector.getStatistics());
            } else {
                System.out.printf("%s: no data%n", dt);
            }
        }
    }

    public void closeAllFiles() {
        for (BufferedWriter writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.printf("Error while closing file: %s%n", e.getMessage());
            }
        }
        writers.clear();
    }
}
