package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractParser<T extends Object> implements Parser<T> {

    protected T[] limitedTo;
    protected Collection<Key<? extends BaseValue<T>>> limitedToKeys;

    public AbstractParser(){
        this(new ArrayList<>());
    }

    public AbstractParser(Collection<Key<? extends BaseValue<T>>> collection, T... values){
        this.limitedTo = values;
        this.limitedToKeys = collection;
    }

    @Override
    public Collection<Key<? extends BaseValue<T>>> getDesginedForKeys() {
        return limitedToKeys;
    }

    @Override
    public T[] getLimitedTo() {
        return limitedTo;
    }
}
