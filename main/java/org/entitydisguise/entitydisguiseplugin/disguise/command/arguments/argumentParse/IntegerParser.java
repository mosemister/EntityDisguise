package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

public class IntegerParser implements Parser<Integer> {

    Integer[] limitedTo;

    public IntegerParser(Integer... limitedTo){
        this.limitedTo = limitedTo;
    }

    @Override
    public Class<Integer> getParseType() {
        return Integer.class;
    }

    @Override
    public Integer parse(String value) throws CommandException {
        try {
            return Integer.parseInt(value);
        }catch(NumberFormatException e){
            throw new CommandException(Text.of("A whole number is required"));
        }
    }

    @Override
    public List<String> getSuggested(String value) {
        return new ArrayList<>();
    }

    @Override
    public Parser<Integer> copy(Integer... limitedTo) {
        return new IntegerParser(limitedTo);
    }

    @Override
    public Integer[] getLimitedTo() {
        return limitedTo;
    }
}
