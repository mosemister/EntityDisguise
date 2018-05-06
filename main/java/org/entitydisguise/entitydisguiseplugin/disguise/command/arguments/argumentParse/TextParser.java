package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextParser extends AbstractParser<LiteralText> implements Parser<LiteralText> {

    public TextParser(){
        super();
    }

    public TextParser(Collection<Key<? extends BaseValue<LiteralText>>> collection, LiteralText... limitedTo){
        super(collection, limitedTo);
    }

    @Override
    public Class<LiteralText> getParseType() {
        return LiteralText.class;
    }

    @Override
    public LiteralText parse(String value) {
        String replaced = value;
        for(TextColor text : Sponge.getRegistry().getAllOf(TextColor.class)){
            String colour = "%" + text.getName() + "%";
            if(replaced.contains(colour)){
                replaced = replaced.replace(colour, text.toString());
            }
        }
        return Text.of(replaced);
    }

    @Override
    public List<String> getSuggested(String value) {
        List<String> suggestion = new ArrayList<>();
        for(Text limit : getLimitedTo()){
            if(limit.toPlain().contains(value)){
                suggestion.add(limit.toPlain());
            }
        }
        return suggestion;
    }

}
