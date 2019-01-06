package com.android.live.filament.android;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER, ElementType.METHOD,
        ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface Entity {
    int NULL = 0;
}
