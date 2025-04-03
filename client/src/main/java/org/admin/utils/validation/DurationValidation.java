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
        String[] durationArr = duration.split(":");
        if(durationArr.length != 2) return false;

        try {
            LocalTime durationLocalTime = LocalTime.of(Integer.parseInt(durationArr[0]), Integer.parseInt(durationArr[1]));
            System.out.println(durationLocalTime);
            return durationLocalTime.getMinute() % 30 == 0 && durationLocalTime.getHour() < 14;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
