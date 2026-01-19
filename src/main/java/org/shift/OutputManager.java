package org.shift;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;

public class OutputManager {
    private final Path outputDirectoryName;

    private final String outputFileNamePrefix;

    private final boolean isAppendToExistingFiles;

    private final Map<DataType, BufferedWriter> writers = new EnumMap<>(DataType.class);

    public OutputManager(Path outputDirectoryName, String outputFileNamePrefix, boolean isAppendToExistingFiles) {
        this.isAppendToExistingFiles = isAppendToExistingFiles;
        this.outputDirectoryName = outputDirectoryName;
        this.outputFileNamePrefix = outputFileNamePrefix;
    }

    private String getFileNameForDataType(DataType dataType) {
        return switch (dataType) {
            case INTEGER -> outputFileNamePrefix + "integers.txt";
            case FLOAT -> outputFileNamePrefix + "floats.txt";
            case STRING -> outputFileNamePrefix + "strings.txt";
        };
    }

    private BufferedWriter createWriter(DataType dataType) throws IOException {
        Files.createDirectories(outputDirectoryName);

        Path path = outputDirectoryName.resolve(getFileNameForDataType(dataType));

        OpenOption[] fileMode = isAppendToExistingFiles
                ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND}
                : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};

        return Files.newBufferedWriter(path, java.nio.charset.StandardCharsets.UTF_8, fileMode);
    }

    public void writeLine(DataType dataType, String line) throws IOException {
        BufferedWriter writer = writers.get(dataType);

        if (writer == null) {
            writer = createWriter(dataType);
            writers.put(dataType, writer);
        }

        writer.write(line);
        writer.newLine();
    }

    public void closeAllFiles() {
        for (BufferedWriter writer : writers.values()) {
            try {
                writer.close();
            }
            catch (IOException e) {
                System.err.printf("Error while closing file: %s%n", e.getMessage());
            }
        }
    }
}
