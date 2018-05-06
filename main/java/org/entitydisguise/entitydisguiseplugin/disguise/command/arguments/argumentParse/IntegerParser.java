package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IntegerParser extends AbstractParser<Integer> implements Parser<Integer> {

    public IntegerParser(){
        limitedTo = new Integer[0];
    }

    public IntegerParser(Collection<Key<? extends BaseValue<Integer>>> collection, Integer... limitedTo){
        super(collection, limitedTo);
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

}
