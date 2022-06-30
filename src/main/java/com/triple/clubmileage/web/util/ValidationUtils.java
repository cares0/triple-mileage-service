package com.triple.clubmileage.web.util;

import com.triple.clubmileage.web.exception.ValidationFailException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidationUtils {

    public void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> map = new HashMap<>();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                String defaultMessage = objectError.getDefaultMessage();
                FieldError fieldError = (FieldError) objectError;
                String fieldName = fieldError.getField();
                map.put(fieldName, defaultMessage);
            }
            throw new ValidationFailException(map.toString());
        }
    }
}
