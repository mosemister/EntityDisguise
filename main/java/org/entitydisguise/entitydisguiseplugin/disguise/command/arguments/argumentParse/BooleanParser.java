package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BooleanParser extends AbstractParser<Boolean> implements Parser<Boolean> {

    public BooleanParser(){
        super();
    }

    public BooleanParser(Collection<Key<? extends BaseValue<Boolean>>> keys, Boolean... limitedTo){
        super(keys, limitedTo);
    }

    @Override
    public Class<Boolean> getParseType() {
        return Boolean.class;
    }

    @Override
    public Boolean parse(String value){
        return Boolean.parseBoolean(value);
    }

    @Override
    public List<String> getSuggested(String value) {
        if("true".startsWith(value)){
            return Arrays.asList("true");
        }else if("false".startsWith(value)){
            return Arrays.asList("false");
        }
        return new ArrayList<>();
    }
}
