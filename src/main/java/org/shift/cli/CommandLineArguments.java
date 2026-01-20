package org.shift.cli;

import org.shift.stats.StatisticsType;
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
    private boolean isShortStatistic;

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
        if (isShortStatistic && isFullStatistic) {
            throw new CommandLine.ParameterException(
                    new CommandLine(this),
                    "Options -s and -f cannot be used together"
            );
        }

        if (isShortStatistic) {
            statisticsType = StatisticsType.SHORT;
        } else if (isFullStatistic) {
            statisticsType = StatisticsType.FULL;
        }

        inputFileNames.removeIf(path -> {
            if (!path.toFile().exists()) {
                System.err.printf(
                        "Input file does not exist: %s%n",
                        path
                );
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

    public void setStatisticsType(StatisticsType statisticsType) {
        this.statisticsType = statisticsType;
    }

    public List<Path> getInputFileNames() {
        return inputFileNames;
    }

    public void setInputFileNames(List<Path> inputFileNames) {
        this.inputFileNames = inputFileNames;
    }

    public Path getOutputDirectoryName() {
        return outputDirectoryName;
    }

    public void setOutputDirectoryName(Path outputDirectoryName) {
        this.outputDirectoryName = outputDirectoryName;
    }

    public String getOutputFileNamePrefix() {
        return outputFileNamePrefix;
    }

    public void setOutputFileNamePrefix(String outputFileNamePrefix) {
        this.outputFileNamePrefix = outputFileNamePrefix;
    }

    public boolean getIsAppendToExistingFiles() {
        return isAppendToExistingFiles;
    }

    public void setAppendToExistingFiles(boolean append) {
        this.isAppendToExistingFiles = append;
    }
}
