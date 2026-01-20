package org.shift;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.shift.core.DataFilter;
import org.shift.core.DataType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataFilterTest {

    DataFilter dataFilter = new DataFilter();

    @ParameterizedTest
    @CsvSource({
            "0, INTEGER",
            "123, INTEGER",
            "-456, INTEGER",
            "12345678901234567890, INTEGER"
    })
    void testDefiningIntegerValues(String inputString, DataType expectedDataType) {
        assertEquals(expectedDataType, dataFilter.defineFileStringDataType(inputString));
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, FLOAT",
            "123.123, FLOAT",
            "-456.123, FLOAT",
            "12345678901234567890.543123, FLOAT"
    })
    void testDefiningFloatValuesWithPoint(String inputString, DataType expectedDataType) {
        assertEquals(expectedDataType, dataFilter.defineFileStringDataType(inputString));
    }

    @ParameterizedTest
    @CsvSource({
            "0E1, FLOAT",
            "0e1, FLOAT",
            "-456E123, FLOAT",
            "12345678901234567890e-12, FLOAT"
    })
    void testDefiningFloatValuesWithExponent(String inputString, DataType expectedDataType) {
        assertEquals(expectedDataType, dataFilter.defineFileStringDataType(inputString));
    }

    @ParameterizedTest
    @CsvSource({
            "0.1E1, FLOAT",
            "0.4e1, FLOAT",
            "-456.123E123, FLOAT",
            "12345678901.234567890e-12, FLOAT"
    })
    void testDefiningFloatValuesWithPointAndExponent(String inputString, DataType expectedDataType) {
        assertEquals(expectedDataType, dataFilter.defineFileStringDataType(inputString));
    }

    @ParameterizedTest
    @CsvSource({
            "+1, STRING",
            "--2, STRING",
            "abc, STRING",
            "abc bvc, STRING",
            "1., STRING",
            ".2, STRING",
            "1.2.3, STRING"
    })
    void testDefiningStringValues(String inputString, DataType expectedDataType) {
        assertEquals(expectedDataType, dataFilter.defineFileStringDataType(inputString));
    }
}
