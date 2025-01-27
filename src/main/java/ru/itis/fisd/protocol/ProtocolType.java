package ru.itis.fisd.protocol;

import lombok.Getter;

@Getter
public enum ProtocolType {

    PING("0"), INFO("1"), ERROR("2"), GAME("3");

    private String value;

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
