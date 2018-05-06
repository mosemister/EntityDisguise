package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BooleanParser implements Parser<Boolean> {

    Boolean[] limitedTo;

    public BooleanParser(Boolean... limitedTo){
        this.limitedTo = limitedTo;
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

    @Override
    public Parser<Boolean> copy(Boolean... limitedTo) {
        return new BooleanParser(limitedTo);
    }

    @Override
    public Boolean[] getLimitedTo() {
        return limitedTo;
    }
}
