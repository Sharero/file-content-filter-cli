package org.shift;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FilesFilter {

    private final DataFilter dataFilter;

    private final StatisticsType statisticsType;

    private final OutputManager outputManager;

    private final List<Path> inputFileNames;

    public FilesFilter(OutputManager outputManager, CommandLineArguments commandLineArguments) {
        this.outputManager = outputManager;
        this.statisticsType = commandLineArguments.getStatisticsType();
        this.inputFileNames = commandLineArguments.getInputFileNames();
        this.dataFilter = new DataFilter();
    }

    public void filterFilesData() {
        for (Path filePath : inputFileNames) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String row;
                while ((row = reader.readLine()) != null) {
                    String trimmedRow = row.trim();
                    DataType dataType = dataFilter.defineFileStringDataType(trimmedRow);

                    outputManager.writeLine(dataType, row);
                }
            } catch (IOException e) {
                System.err.printf("Error reading file %s: %s%n", filePath, e.getMessage());
            }
        }

        outputManager.closeAllFiles();
    }
}
