package org.shift;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shift.cli.CommandLineArguments;
import org.shift.core.DataFilter;
import org.shift.core.DataType;
import org.shift.core.FilesFilter;
import org.shift.core.OutputManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FilesFilterTest {

    private OutputManager outputManager;

    private DataFilter dataFilter;

    @BeforeEach
    void setUp() {
        CommandLineArguments commandLineArguments = new CommandLineArguments();

        commandLineArguments.setAppendToExistingFiles(false);
        commandLineArguments.setOutputDirectoryName(null);
        commandLineArguments.setOutputFileNamePrefix("");

        outputManager = new OutputManager(commandLineArguments);

        dataFilter = new DataFilter();
    }

    @Test
    void testFilterLineWritesCorrectType() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);

        Map<DataType, BufferedWriter> writers = new EnumMap<>(DataType.class);
        writers.put(DataType.INTEGER, bufferedWriter);

        outputManager.setWriters(writers);

        FilesFilter filesFilter = new FilesFilter(outputManager, new CommandLineArguments(), dataFilter);

        filesFilter.filterLine("42");

        bufferedWriter.flush();
        assertEquals("42\n", stringWriter.toString());
    }
}
