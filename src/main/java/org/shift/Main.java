import picocli.CommandLine;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {

        CommandLineArguments commandLineArguments = new CommandLineArguments();
        CommandLine commandLine = new CommandLine(commandLineArguments);

        commandLine.parseArgs(args);
        commandLineArguments.run();

        System.out.println(commandLineArguments.outputDirectoryName);
        System.out.println(commandLineArguments.outputFileNamePrefix);
        System.out.println(commandLineArguments.isAppendToExistingFiles);
        System.out.println(commandLineArguments.isBriefStatistic);
        System.out.println(commandLineArguments.isFullStatistic);

        for (Path path : commandLineArguments.inputFileNames) {
            System.out.println(path);
        }
    }
}