package com.dolplads.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/**
 * Created by dolplads on 27/09/16.
 */
@NotNull
@Constraint(validatedBy = PersistedValidator.class)
@Target({
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE}
)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Persisted {
    String message() default "User must be persisted";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
