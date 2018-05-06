package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import java.util.List;

public class StringParser implements Parser<String> {

    String[] limitedTo;

    public StringParser(String... limitedTo){
        this.limitedTo = limitedTo;
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
        return null;
    }

    @Override
    public Parser<String> copy(String... limitedTo) {
        return new StringParser(limitedTo);
    }

    @Override
    public String[] getLimitedTo() {
        return limitedTo;
    }
}
