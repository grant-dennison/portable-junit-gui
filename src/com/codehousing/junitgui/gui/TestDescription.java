package com.codehousing.junitgui.gui;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestDescription {
    String value();
}
