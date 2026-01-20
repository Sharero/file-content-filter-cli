package org.shift;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shift.cli.CommandLineArguments;
import org.shift.core.DataType;
import org.shift.core.OutputManager;
import org.shift.stats.StatisticsCollector;
import org.shift.stats.StatisticsType;

import java.io.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OutputManagerTest {

    @Mock
    StatisticsCollector integerCollector;

    @Mock
    StatisticsCollector floatCollector;

    @Mock
    StatisticsCollector stringCollector;

    OutputManager outputManager;

    @BeforeEach
    void setUp() {
        CommandLineArguments commandLineArguments = new CommandLineArguments();

        commandLineArguments.setAppendToExistingFiles(false);
        commandLineArguments.setOutputDirectoryName(null);
        commandLineArguments.setOutputFileNamePrefix("");

        outputManager = new OutputManager(commandLineArguments);
    }

    @Test
    void testPrintStatisticsWithNoData() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        outputManager.printStatistics();

        List<String> expectedLines = List.of(
                "=== Statistics ===",
                "INTEGER: no data",
                "FLOAT: no data",
                "STRING: no data"
        );

        String actual = outContent.toString().replace("\r\n", "\n").trim();
        List<String> actualLines = List.of(actual.split("\n"));

        assertLinesMatch(expectedLines, actualLines);

        System.setOut(originalOut);
    }

    @Test
    void testPrintShortStatisticsWithData() {
        when(integerCollector.getStatistics()).thenReturn("count: 1");
        when(floatCollector.getStatistics()).thenReturn("count: 1");
        when(stringCollector.getStatistics()).thenReturn("count: 1");

        Map<DataType, StatisticsCollector> testStatistics = new EnumMap<>(DataType.class);

        testStatistics.put(DataType.INTEGER, integerCollector);
        testStatistics.put(DataType.FLOAT, floatCollector);
        testStatistics.put(DataType.STRING, stringCollector);

        outputManager.setStatistics(testStatistics);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        outputManager.printStatistics();

        List<String> expectedLines = List.of(
                "=== Statistics ===",
                "INTEGER: count: 1",
                "FLOAT: count: 1",
                "STRING: count: 1"
        );

        String actual = outContent.toString().replace("\r\n", "\n").trim();
        List<String> actualLines = List.of(actual.split("\n"));

        assertLinesMatch(expectedLines, actualLines);

        System.setOut(originalOut);
    }

    @Test
    void testPrintFullStatisticsWithData() {
        when(integerCollector.getStatistics()).thenReturn("count: 2, Min: 2, Max: 4, Sum: 6, Avg:" +
                " 3.0");
        when(floatCollector.getStatistics()).thenReturn("count: 1, Min: 1.0, Max: 1.0, Sum: 1.0, " +
                "Avg: 1.0");
        when(stringCollector.getStatistics()).thenReturn("count: 1, MinLength: 4, MaxLength: 4");

        Map<DataType, StatisticsCollector> testStatistics = new EnumMap<>(DataType.class);

        testStatistics.put(DataType.INTEGER, integerCollector);
        testStatistics.put(DataType.FLOAT, floatCollector);
        testStatistics.put(DataType.STRING, stringCollector);

        outputManager.setStatistics(testStatistics);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        outputManager.printStatistics();

        List<String> expectedLines = List.of(
                "=== Statistics ===",
                "INTEGER: count: 2, Min: 2, Max: 4, Sum: 6, Avg: 3.0",
                "FLOAT: count: 1, Min: 1.0, Max: 1.0, Sum: 1.0, Avg: 1.0",
                "STRING: count: 1, MinLength: 4, MaxLength: 4"
        );

        String actual = outContent.toString().replace("\r\n", "\n").trim();
        List<String> actualLines = List.of(actual.split("\n"));

        assertLinesMatch(expectedLines, actualLines);

        System.setOut(originalOut);
    }

    @Test
    void testCloseAllFiles() throws IOException {
        StringWriter stringWriter1 = new StringWriter();
        StringWriter stringWriter2 = new StringWriter();

        BufferedWriter writer1 = new BufferedWriter(stringWriter1);
        BufferedWriter writer2 = new BufferedWriter(stringWriter2);

        Map<DataType, BufferedWriter> testWriter = new EnumMap<>(DataType.class);

        testWriter.put(DataType.INTEGER, writer1);
        testWriter.put(DataType.STRING, writer2);

        outputManager.setWriters(testWriter);

        outputManager.closeAllFiles();

        assertTrue(outputManager.getWriters().isEmpty());

        assertThrows(IOException.class, () -> writer1.write("test"));
        assertThrows(IOException.class, () -> writer2.write("test"));
    }


    @Test
    void testWriteLineSkipsDisabledTypeWriter() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BufferedWriter writer = new BufferedWriter(stringWriter) {
            @Override
            public void write(String s) throws IOException {
                throw new IOException("Failed to write data to file");
            }
        };

        Map<DataType, BufferedWriter> testWriter = new EnumMap<>(DataType.class);

        testWriter.put(DataType.INTEGER, writer);

        outputManager.setWriters(testWriter);

        outputManager.writeLine(DataType.INTEGER, "123");
        outputManager.writeLine(DataType.INTEGER, "1232");

        assertEquals(0, outputManager.getWriters().size());
    }

    @Test
    void testWriteLineWritesToWriter() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);

        Map<DataType, BufferedWriter> testWriter = new EnumMap<>(DataType.class);

        testWriter.put(DataType.INTEGER, writer);

        outputManager.setWriters(testWriter);

        outputManager.writeLine(DataType.INTEGER, "42");

        writer.flush();

        assertEquals("42\n", stringWriter.toString());
    }

    @Test
    void testWriteLineAddsValueToCollector() throws IOException {
        outputManager.setStatisticsType(StatisticsType.SHORT);

        Map<DataType, StatisticsCollector> testStatistics = new EnumMap<>(DataType.class);

        testStatistics.put(DataType.INTEGER, integerCollector);

        outputManager.setStatistics(testStatistics);

        outputManager.writeLine(DataType.INTEGER, "42");

        verify(integerCollector).addValue("42");
    }

    @Test
    void testWriteLineCreatesWriterIfAbsent() throws IOException {
        assertTrue(outputManager.getWriters().isEmpty());

        outputManager.writeLine(DataType.INTEGER, "42");

        assertTrue(outputManager.getWriters().containsKey(DataType.INTEGER));
    }
}
