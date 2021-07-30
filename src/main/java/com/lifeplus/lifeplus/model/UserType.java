package com.lifeplus.lifeplus.model;

public enum UserType {
    ADMIN,
    MEDIC,
    KINESIOLOGIST,
    USER;

    public static String commaSeparated(UserType[] types) {
        StringBuilder result = new StringBuilder();
        for (UserType type : types) {
            result.append(toString(type)).append(',');
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public static String toString(UserType userType) {
        switch (userType) {
            case ADMIN:
                return "ADMIN";
            case MEDIC:
                return "MEDIC";
            case KINESIOLOGIST:
                return "KINESIOLOGIST";
            case USER:
                return "USER";
        }
        return "";
    }
}
