package com.CezaryZal.validation.validator;

import com.CezaryZal.validation.annotation.Timeline;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class TimelineConstraintValidator implements ConstraintValidator<Timeline, LocalTime> {


    @Override
    public boolean isValid(LocalTime inputLocalTime, ConstraintValidatorContext context) {
        if (inputLocalTime == null){
            return true;
        }
        return !inputLocalTime.equals(LocalTime.of(0,0)) &&
                inputLocalTime.isBefore(LocalTime.of(3,0));
    }
}
