package de.frohwerk.demo.test;

import de.frohwerk.demo.simple.Simple;
import de.frohwerk.demo.simple.SimpleBase;
import de.frohwerk.demo.simple.SimpleImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleTest {
    @Test
    void basicUseCase() {
        final var simpleObject = new SimpleImpl();
        final var decorator = new TestDecorator(simpleObject);

        assertThat(decorator).isInstanceOf(Simple.class);

        assertThat(simpleObject.greeting()).isEqualTo("Hello World");
        assertThat(simpleObject.name()).isEqualTo("de.frohwerk.demo.simple.SimpleImpl");

        assertThat(decorator.greeting()).isEqualTo("Hello Test");
        assertThat(decorator.name()).isEqualTo("de.frohwerk.demo.simple.SimpleImpl");
    }

    private static class TestDecorator extends SimpleBase {

        public TestDecorator(final Simple delegate) {
            super(delegate);
        }

        @Override
        public String greeting() {
            return "Hello Test";
        }
    }

}
