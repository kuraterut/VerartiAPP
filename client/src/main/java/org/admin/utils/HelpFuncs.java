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

    public static Boolean checkPhone(String phone) {
        if (phone.length() != 12 || !phone.startsWith("+7")){
            for(int i = 1; i < 12; i++){
                if(!Character.isDigit(phone.charAt(i))){
                    return false;
                }
            }
        }
        return true;
    }

    public static Boolean checkEmail(String email) {
        return email.contains("@");
    }
}
