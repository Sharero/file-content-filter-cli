package org.shift;

import picocli.CommandLine;

import java.nio.file.Path;
import java.util.List;

@CommandLine.Command(
        name = "util",
        mixinStandardHelpOptions = true,
        description = "Filters file contents by data type to specific files for each type"
)
public class CommandLineArguments implements Runnable {

    @CommandLine.Option(
            names = "-o",
            description = "Set specific output directory name"
    )
    Path outputDirectoryName;

    @CommandLine.Option(
            names = "-p",
            description = "Set specific output file name prefix"
    )
    String outputFileNamePrefix = "";

    @CommandLine.Option(
            names = "-a",
            description = "New data will be added to existing files"
    )
    boolean isAppendToExistingFiles;

    @CommandLine.Option(
            names = "-s",
            description = "Output brief statistic about util work results"
    )
    boolean isBriefStatistic;

    @CommandLine.Option(
            names = "-f",
            description = "Output full statistic about util work results"
    )
    boolean isFullStatistic;

    @CommandLine.Parameters(
            arity = "1..*",
            paramLabel = "FILES",
            description = "Input files"
    )
    List<Path> inputFileNames;

    StatisticsType statisticsType;

    @Override
    public void run() {
        if (isFullStatistic) {
            statisticsType = StatisticsType.FULL;
        } else {
            statisticsType = StatisticsType.BRIEF;
        }
    }
}
