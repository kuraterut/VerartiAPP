package org.admin.utils.validation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PriceValidation implements Validation {
    String price;
    @Override
    public boolean validate() {
        if(price == null || price.isEmpty()) return false;
        for(int i = 0; i < price.length(); i++) {
            if(!Character.isDigit(price.charAt(i))) return false;
        }
        return true;
    }
}
