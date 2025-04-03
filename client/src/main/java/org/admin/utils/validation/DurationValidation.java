package org.admin.utils.validation;

import lombok.AllArgsConstructor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
public class DurationValidation implements Validation {
    String duration;

    @Override
    public boolean validate() {
        if(duration == null || duration.isEmpty()) return false;
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime durationLocalTime = LocalTime.parse(duration, inputFormatter);
            return durationLocalTime.getMinute() % 30 == 0 && durationLocalTime.getHour() < 14;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
