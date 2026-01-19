package org.shift;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FilesFilter {

    private DataFilter dataFilter;

    private StatisticsType statisticsType;

    private OutputManager outputManager;

    private List<Path> inputFileNames;

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
                    String trimmed_row = row.trim();

                    DataType dataType = dataFilter.defineFileStringDataType(trimmed_row);

                    switch (dataType) {
                        case INTEGER:
                            try {
                                outputManager.writeLine(dataType, row);
                            } catch (IOException e) {
                                System.out.printf("Failed to write integer line to output for line: " + row + " (" + e.getMessage() + ")");
                            }
                            break;
                        case FLOAT:
                            try {
                                outputManager.writeLine(dataType, row);
                            } catch (IOException e) {
                                System.out.printf("Failed to write float line to output for line: " + row + " (" + e.getMessage() + ")");
                            }
                            break;
                        case STRING:
                            try {
                                outputManager.writeLine(dataType, row);
                            } catch (IOException e) {
                                System.out.printf("Failed to write string line to output for line: " + row + " (" + e.getMessage() + ")");
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.printf("Error %s in file %s", e.getMessage(), filePath);
            }
        }
        outputManager.closeAllFiles();
    }
}
