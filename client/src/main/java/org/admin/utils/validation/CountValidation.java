package org.admin.utils.validation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CountValidation implements Validation {
    String count;

    @Override
    public boolean validate() {
        if(count == null || count.isEmpty()) return false;
        for(int i = 0; i < count.length(); i++) {
            if(!Character.isDigit(count.charAt(i))) return false;
        }
        return true;
    }
}
