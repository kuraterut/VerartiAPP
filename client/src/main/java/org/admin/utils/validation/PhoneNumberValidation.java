package org.admin.utils.validation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PhoneNumberValidation implements Validation {
    String phoneNumber;

    //+79092762462
    @Override
    public boolean validate() {
        if(phoneNumber == null || phoneNumber.isEmpty()) return false;
        if(phoneNumber.length() != 12) return false;
        if(phoneNumber.charAt(0) != '+') return false;
        for(int i = 1; i < phoneNumber.length(); i++) {
            if(!Character.isDigit(phoneNumber.charAt(i))) return false;
        }
        return true;
    }
}
