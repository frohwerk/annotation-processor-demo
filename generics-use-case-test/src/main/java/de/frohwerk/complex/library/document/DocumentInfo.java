package de.frohwerk.complex.library.document;

import de.frohwerk.complex.library.core.Linkable;

public interface DocumentInfo extends Linkable<DocumentInfo> {

    String getName();

    void setName(final String name);

}
