package com.chocolate.moudle.scan.my;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({DetectResult.HIGH, DetectResult.MEDIUM, DetectResult.NONE})
@Retention(RetentionPolicy.SOURCE)
public @interface DetectResult {
    int HIGH = 2;
    int MEDIUM = 1;
    int NONE = 0;
}