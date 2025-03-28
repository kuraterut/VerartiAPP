package org.admin.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HelpFuncs {
    public static LocalDate stringToLocalDate(String date) {
        return LocalDate.parse(date);
    }
    public static String localDateToString(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }
    public static String localTimeToString(LocalTime time, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return time.format(formatter);
    }

    public static boolean checkPhone(String phone) {
        if (phone.length() == 12 && phone.startsWith("+7")){
            for(int i = 1; i < 12; i++){
                if(!Character.isDigit(phone.charAt(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static Boolean checkEmail(String email) {
        return email.contains("@");
    }
}
