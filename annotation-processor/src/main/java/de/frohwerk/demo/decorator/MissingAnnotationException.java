package de.frohwerk.demo.decorator;

public final class MissingAnnotationException extends IllegalStateException {
    MissingAnnotationException(final Class<?> decoratorClass) {
        super(String.format("Annotation %s is missing on target type", decoratorClass.getName()));
    }
}
