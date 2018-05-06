package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DoubleParse extends AbstractParser<Double> implements Parser<Double> {

    public DoubleParse(){
        super();
    }

    public DoubleParse(Collection<Key<? extends BaseValue<Double>>> collection, Double... limitedTo){
        super(collection, limitedTo);
    }

    @Override
    public Class<Double> getParseType() {
        return Double.class;
    }

    @Override
    public Double parse(String value) throws CommandException{
        try{
            return Double.parseDouble(value);
        }catch(NumberFormatException e){
            try {
                return (Double) (double) Integer.parseInt(value);
            }catch(NumberFormatException e1){
                throw new CommandException(Text.of("A number is required"));
            }
        }
    }

    @Override
    public List<String> getSuggested(String value) {
        return new ArrayList<>();
    }
}
