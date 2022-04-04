package de.frohwerk.complex.library.core;

import de.frohwerk.complex.library.document.DocumentInfo;

public class LinkImpl<A> implements Link<A> {

    private String value;

    protected LinkImpl(final String value) {
        this.value = value;
    }

    public static Link<DocumentInfo> create(final String value) {
        return new LinkImpl<>(value);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(final String value) {
        this.value = value;
    }

}
