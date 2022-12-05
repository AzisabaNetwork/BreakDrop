package net.azisaba.breakdrop.config;

import org.jetbrains.annotations.NotNull;

public enum VariableType {
    STRING,
    INT,
    FLOAT,
    ;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void checkType(@NotNull String value) {
        switch (this) {
            case STRING:
                break;
            case INT:
                Integer.parseInt(value);
                break;
            case FLOAT:
                Float.parseFloat(value);
                break;
        }
    }
}
