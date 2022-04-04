package de.frohwerk.demo.decorator;

public class Preconditions {

    private Preconditions() {}

    public static void checkState(final boolean state, final String message, final Object... args) {
        if (!state) throw new IllegalStateException(String.format(message, args));
    }

}
