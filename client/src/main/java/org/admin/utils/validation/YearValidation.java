package org.admin.utils.validation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class YearValidation implements Validation {
    String yearStr;

    @Override
    public boolean validate() {
        if(yearStr == null || yearStr.isEmpty()) return false;
        for(int i = 0; i < yearStr.length(); i++) {
            if(!Character.isDigit(yearStr.charAt(i))) return false;
        }
        int year = Integer.parseInt(yearStr);
        return year >= 1900 && year <= 2100;
    }
}
