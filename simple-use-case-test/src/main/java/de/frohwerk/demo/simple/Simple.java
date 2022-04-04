package de.frohwerk.demo.simple;

import de.frohwerk.demo.decorator.DecoratorBase;

@DecoratorBase(Simple.class)
public interface Simple {

    String name();

    String greeting();

}
