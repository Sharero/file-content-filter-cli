package org.shift;

import java.util.regex.Pattern;

public class DataFilter {

    private final Pattern integerPattern = Pattern.compile("^-?\\d+$");

    private final Pattern floatWithPointPattern = Pattern.compile("^-?\\d+\\.\\d+([eE][+-]?\\d+)?$");

    private final Pattern floatWithoutPointPattern = Pattern.compile("^-?\\d+[eE][+-]?\\d+$");

    public DataType defineFileStringDataType(String fileString) {
        boolean isFloatFound = floatWithoutPointPattern.matcher(fileString).matches() || floatWithPointPattern.matcher(fileString).matches();
        boolean isIntegerFound = integerPattern.matcher(fileString).matches();

        if (isFloatFound) {
            return DataType.FLOAT;
        } else if (isIntegerFound) {
            return DataType.INTEGER;
        }

        return DataType.STRING;
    }
}
