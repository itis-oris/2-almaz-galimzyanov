package ru.itis.fisd.protocol;

import lombok.Getter;

@Getter
public enum ProtocolType {

    CLOSE("0"), UI("1"), INFO("10"), ERROR("-1"), GAME("2"), GET("111"), DELETE("666");

    private final String value;

    ProtocolType(String i) {
        value = i;
    }

    public static ProtocolType fromValue(String value) {
        for (ProtocolType type : ProtocolType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}
