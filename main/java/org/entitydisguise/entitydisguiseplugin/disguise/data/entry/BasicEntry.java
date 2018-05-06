package org.entitydisguise.entitydisguiseplugin.disguise.data.entry;

import java.util.Map;

public class BasicEntry<A extends Object, B extends Object> implements Map.Entry<A, B> {

    A a;
    B b;

    public BasicEntry(A obj, B obj2){
        a = obj;
        b = obj2;
    }

    @Override
    public A getKey() {
        return a;
    }

    @Override
    public B getValue() {
        return b;
    }

    @Override
    public B setValue(B value) {
        b = value;
        return b;
    }
}
