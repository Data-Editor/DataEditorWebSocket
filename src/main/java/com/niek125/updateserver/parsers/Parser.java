package com.niek125.updateserver.parsers;

public interface Parser<T> {
    T parse(String message);
    Class getEventTargetClass(T object);
}
