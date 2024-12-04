package com.example.backend.enums;

public enum Role {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private final String value;

    Role(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}

