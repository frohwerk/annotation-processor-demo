package de.frohwerk.complex.library.core;

public interface Linkable<A> {

    Link<A> getSelf();

    void setSelf(final Link<A> self);

}
