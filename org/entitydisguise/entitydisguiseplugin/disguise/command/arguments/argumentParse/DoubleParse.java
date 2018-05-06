package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DoubleParse implements Parser<Double> {

    Double[] limitedTo;

    public DoubleParse(Double... limitedTo){
        this.limitedTo = limitedTo;
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

    @Override
    public Parser<Double> copy(Double... limitedTo) {
        return new DoubleParse(limitedTo);
    }

    @Override
    public Double[] getLimitedTo() {
        return limitedTo;
    }
}
