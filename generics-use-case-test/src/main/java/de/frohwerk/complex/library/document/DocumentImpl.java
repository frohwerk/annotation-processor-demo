package de.frohwerk.complex.library.document;

public class DocumentImpl extends DocumentInfoImpl implements Document {

    private String data;

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public void setData(final String data) {
        this.data = data;
    }

}
