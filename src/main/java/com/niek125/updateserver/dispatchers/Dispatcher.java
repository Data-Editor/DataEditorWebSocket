package com.niek125.updateserver.dispatchers;

public interface Dispatcher<T> {
    void dispatch(T message);
}
