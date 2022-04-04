package de.frohwerk.complex.library.core;

public class LinkableImpl<A> implements Linkable<A> {

    private Link<A> self;

    @Override
    public Link<A> getSelf() {
        return self;
    }

    @Override
    public void setSelf(Link<A> self) {
        this.self = self;
    }

}
