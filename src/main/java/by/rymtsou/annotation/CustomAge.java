package by.rymtsou.annotation;

import by.rymtsou.service.validation.AgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = AgeValidator.class)
public @interface CustomAge {
    String message() default "Age is invalid! Should be not null and between 18 and 120";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
