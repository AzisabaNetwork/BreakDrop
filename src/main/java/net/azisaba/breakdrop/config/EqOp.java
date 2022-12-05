package net.azisaba.breakdrop.config;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum EqOp {
    EQ,  // =
    NE,  // !=
    LT,  // <
    GT,  // >
    LE, // <=
    GE, // >=
    ;

    public static @NotNull EqOp of(@NotNull String op) {
        switch (op) {
            case "=":
                return EQ;
            case "!=":
                return NE;
            case "<":
                return LT;
            case ">":
                return GT;
            case "<=":
                return LE;
            case ">=":
                return GE;
            default:
                return valueOf(op.toUpperCase(Locale.ROOT));
        }
    }
}
