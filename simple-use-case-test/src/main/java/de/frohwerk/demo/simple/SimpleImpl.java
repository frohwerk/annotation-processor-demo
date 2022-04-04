package de.frohwerk.demo.simple;

public class SimpleImpl implements Simple {
    @Override
    public String name() {
        return getClass().getName();
    }

    @Override
    public String greeting() {
        return "Hello World";
    }
}
