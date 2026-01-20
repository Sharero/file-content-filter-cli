package org.shift;

import org.shift.cli.CommandLineArguments;
import org.shift.core.DataFilter;
import org.shift.core.FilesFilter;
import org.shift.core.OutputManager;
import picocli.CommandLine;

public class Main {

    public static void main(String[] args) {

        CommandLineArguments commandLineArguments = new CommandLineArguments();
        CommandLine commandLine = new CommandLine(commandLineArguments);

        commandLine.parseArgs(args);
        commandLineArguments.run();

        OutputManager outputManager = new OutputManager(commandLineArguments);
        DataFilter dataFilter = new DataFilter();
        FilesFilter filesFilter = new FilesFilter(outputManager, commandLineArguments, dataFilter);

        filesFilter.filterFilesData();
    }
}