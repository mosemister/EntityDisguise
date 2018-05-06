package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringParser extends AbstractParser<String> implements Parser<String> {

    public StringParser(){
        super();
    }

    public StringParser(Collection<Key<? extends BaseValue<String>>> collection, String... limitedTo){
        super(collection, limitedTo);
    }

    @Override
    public Class<String> getParseType() {
        return String.class;
    }

    @Override
    public String parse(String value){
        return value;
    }

    @Override
    public List<String> getSuggested(String value) {
        List<String> suggestion = new ArrayList<>();
        for(String limit : getLimitedTo()){
            if(limit.contains(value)){
                suggestion.add(limit);
            }
        }
        return suggestion;
    }

}
