package com.LSoftwareLTDA.diarioDigital.entidades.enumeracoes;

public enum Role {
    ADMIN(1),
    USER(2);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role fromValue(int value) {
        for (Role role : Role.values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}
