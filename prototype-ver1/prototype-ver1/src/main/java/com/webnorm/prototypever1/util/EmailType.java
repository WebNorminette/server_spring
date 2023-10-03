package com.webnorm.prototypever1.util;

import lombok.Getter;

@Getter
public enum EmailType {
    WELCOME("welcome"), PASSWORD("password");

    private String value;

    EmailType(String value) {
        this.value = value;
    }
}
