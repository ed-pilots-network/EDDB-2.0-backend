package io.edpn.backend.trade.application.domain;

import java.util.NoSuchElementException;

public enum LandingPadSize {
    UNKNOWN(-1),
    SMALL(100),
    MEDIUM(200),
    LARGE(300);

    private final int value;

    LandingPadSize(int value) {
        this.value = value;
    }

    public static LandingPadSize fromInteger(int x) {
        for (LandingPadSize size : LandingPadSize.values()) {
            if (size.value == x) {
                return size;
            }
        }

        throw new NoSuchElementException("integer value %d is not mapped to a landing pad size".formatted(x));
    }

    public int value() {
        return value;
    }
}