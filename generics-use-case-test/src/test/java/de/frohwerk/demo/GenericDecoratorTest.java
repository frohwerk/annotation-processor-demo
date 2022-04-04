package de.frohwerk.demo;

import de.frohwerk.complex.library.core.LinkImpl;
import de.frohwerk.demo.decorator.DecoratorBase;
import de.frohwerk.complex.library.document.Document;
import de.frohwerk.complex.library.document.DocumentImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenericDecoratorTest {
    @Test
    void basicUseCase() {
        final var document = new DocumentImpl();
        document.setName("Hello world");
        document.setData("Hello data");
        document.setSelf(LinkImpl.create("0815"));

        final var decorator = new TestDecorator(document);
        decorator.setName("Hello decorators");
        decorator.setData("Hello override");

        assertThat(document.getName()).isEqualTo("Hello decorators");
        assertThat(document.getData()).isEqualTo("Hello data");

        assertThat(decorator.getName()).isEqualTo("Hello decorators");
        assertThat(decorator.getData()).isEqualTo("Hello override");
    }

    @DecoratorBase(Document.class)
    private static class TestDecorator extends TestDecoratorBase {

        private String dataOverride;

        public TestDecorator(final Document delegate) {
            super(delegate);
        }

        @Override
        public void setData(final String data) {
            this.dataOverride = data;
        }

        @Override
        public String getData() {
            return this.dataOverride != null ? this.dataOverride : super.getData();
        }
    }

}
