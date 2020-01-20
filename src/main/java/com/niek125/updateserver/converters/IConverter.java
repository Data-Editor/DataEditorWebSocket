package com.niek125.updateserver.converters;

public interface IConverter<I, O> {
    Class getConvertType();
    O convert(I object);
}
