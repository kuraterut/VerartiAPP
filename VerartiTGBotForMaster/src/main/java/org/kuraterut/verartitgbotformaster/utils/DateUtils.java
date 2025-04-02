package org.kuraterut.verartitgbotformaster.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    public static String convertToApiFormat(String date) throws DateTimeParseException {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = LocalDate.parse(date, inputFormat);
        return outputFormat.format(localDate);
    }

    public static String convertToUserFormat(String date) throws DateTimeParseException {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDate localDate = LocalDate.parse(date, inputFormat);
        return outputFormat.format(localDate);
    }

    public static String localDateToUserFormat(LocalDate localDate) {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return outputFormat.format(localDate);
    }
    public static String localTimeToUserFormat(LocalTime localTime) {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("HH:mm");

        return outputFormat.format(localTime);
    }
}