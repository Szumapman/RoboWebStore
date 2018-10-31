package com.endlesspowerskills.robowebstore.validator;

import com.endlesspowerskills.robowebstore.entity.Product;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

@Component
public class ProductValidator  implements Validator {

    // -- fields
    @Autowired
    private javax.validation.Validator beanValidator;

    @Autowired
    private final ValuableUnitsInStockValidator valuableUnitsInStockValidator;

    @Setter
    private Set<Validator> springValidators;

    // -- constructors
    public ProductValidator(ValuableUnitsInStockValidator valuableUnitsInStockValidator) {
        this.springValidators = new HashSet<>();
        this.valuableUnitsInStockValidator = valuableUnitsInStockValidator;
        this.springValidators.add(valuableUnitsInStockValidator);

    }


    // -- methods
    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Set<ConstraintViolation<Object>> constraintViolations = beanValidator.validate(target);

        for (ConstraintViolation<Object> constraintViolation : constraintViolations){
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.rejectValue(propertyPath, "", message);
        }

        for (Validator validator :springValidators){
            validator.validate(target, errors);
        }
    }
}
