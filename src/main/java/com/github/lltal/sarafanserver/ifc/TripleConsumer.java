package com.github.lltal.sarafanserver.ifc;

public interface TripleConsumer <E, T, S>{
    void accept(E e, T t, S s);
}
