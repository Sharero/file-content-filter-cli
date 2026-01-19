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
    private Path outputDirectoryName;

    @CommandLine.Option(
            names = "-p",
            description = "Set specific output file name prefix"
    )
    private String outputFileNamePrefix = "";

    @CommandLine.Option(
            names = "-a",
            description = "New data will be added to existing files"
    )
    private boolean isAppendToExistingFiles;

    @CommandLine.Option(
            names = "-s",
            description = "Output brief statistic about util work results"
    )
    private boolean isBriefStatistic;

    @CommandLine.Option(
            names = "-f",
            description = "Output full statistic about util work results"
    )
    private boolean isFullStatistic;

    @CommandLine.Parameters(
            arity = "1..*",
            paramLabel = "FILES",
            description = "Input files"
    )
    private List<Path> inputFileNames;

    private StatisticsType statisticsType = null;

    @Override
    public void run() {
        if (isBriefStatistic && isFullStatistic) {
            throw new CommandLine.ParameterException(
                    new CommandLine(this),
                    "Options -s and -f cannot be used together"
            );
        }

        if (isBriefStatistic) {
            statisticsType = StatisticsType.BRIEF;
        } else if (isFullStatistic) {
            statisticsType = StatisticsType.FULL;
        }

        inputFileNames.removeIf(path -> {
            if (!path.toFile().exists()) {
                System.err.println("Input file does not exist: " + path);
                return true;
            }
            return false;
        });

        if (inputFileNames.isEmpty()) {
            throw new CommandLine.ParameterException(
                    new CommandLine(this),
                    "No valid input files provided"
            );
        }
    }

    public StatisticsType getStatisticsType() {
        return statisticsType;
    }

    public List<Path> getInputFileNames() {
        return inputFileNames;
    }

    public Path getOutputDirectoryName() {
        return outputDirectoryName;
    }

    public String getOutputFileNamePrefix() {
        return outputFileNamePrefix;
    }

    public boolean getIsAppendToExistingFiles() {
        return isAppendToExistingFiles;
    }
}
