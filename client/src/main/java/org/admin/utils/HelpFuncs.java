package org.admin.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HelpFuncs {
    public static LocalDate stringToLocalDate(String date) {
        return LocalDate.parse(date);
    }
    public static String localDateToString(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }
}
