package com.endlesspowerskills.robowebstore.validator;

import com.endlesspowerskills.robowebstore.entity.Product;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ValuableUnitsInStockValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        if(10000 <= product.getPrice() && product.getUnitsInStock() > 10){
            System.out.println(product.getPrice());
            errors.rejectValue("unitsInStock", "valuableUnitsInStockValidator.message");
        }

    }
}
