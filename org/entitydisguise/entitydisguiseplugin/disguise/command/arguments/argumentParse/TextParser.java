package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;

public class TextParser implements Parser<Text> {

    Text[] limitedTo;

    public TextParser(Text... limitedTo){
        this.limitedTo = limitedTo;
    }

    @Override
    public Class<Text> getParseType() {
        return Text.class;
    }

    @Override
    public Text parse(String value) {
        String replaced = value;
        for(TextColor text : Sponge.getRegistry().getAllOf(TextColor.class)){
            String colour = "%" + text.getName() + "%";
            if(replaced.contains(colour)){
                replaced.replace(colour, text.toString());
            }
        }
        return Text.of(replaced);
    }

    @Override
    public List<String> getSuggested(String value) {
        return new ArrayList<>();
    }

    @Override
    public Parser<Text> copy(Text... limitedTo) {
        return new TextParser(limitedTo);
    }

    @Override
    public Text[] getLimitedTo() {
        return limitedTo;
    }
}
