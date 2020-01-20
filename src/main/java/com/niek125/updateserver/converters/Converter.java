package com.niek125.updateserver.converters;

import com.niek125.updateserver.events.DataEditorEvent;

public abstract class Converter<T extends DataEditorEvent, I, O> implements IConverter<I, O> {
    private final Class<T> claz;

    protected Converter(Class<T> claz) {
        this.claz = claz;
    }

    public Class getConvertType() {
        return claz;
    }
}
