package de.frohwerk.complex.library.document;

import de.frohwerk.complex.library.core.LinkableImpl;

public class DocumentInfoImpl extends LinkableImpl<DocumentInfo> implements DocumentInfo {

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
