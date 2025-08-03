package io.github.orionlibs.core;

public interface OrionEnumeration
{
    String get();


    boolean is(OrionEnumeration other);


    boolean isNot(OrionEnumeration other);
}