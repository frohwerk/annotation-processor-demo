package de.frohwerk.demo.decorator;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class Stuff {

    private Stuff() {}

    public static String modelInterfaces(final Object object) {
        return Arrays.stream(object.getClass().getInterfaces())
                .filter(isInPackage("javax.lang.model"))
                .map(Class::getName)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public static String modelInterfaces(final Element element) {
        return Arrays.stream(element.getClass().getInterfaces())
                .filter(isInPackage("javax.lang.model"))
                .map(Class::getName)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    @SuppressWarnings("SameParameterValue")
    private static Predicate<Class<?>> isInPackage(final String packageNamePrefix) {
        return c -> c.getPackageName().startsWith(packageNamePrefix);
    }

}
