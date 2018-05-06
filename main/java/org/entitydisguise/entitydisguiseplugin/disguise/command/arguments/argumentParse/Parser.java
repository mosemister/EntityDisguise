package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Parser <T extends Object> {

    List<Parser<? extends Object>> PARSERS = new ArrayList<>();

    public Class<T> getParseType();
    public Collection<Key<? extends BaseValue<T>>> getDesginedForKeys();
    public T parse(String value) throws CommandException;
    public List<String> getSuggested(String value);
    public T[] getLimitedTo();

    public static <T extends Object> Optional<Parser<T>> getParser(Class<T> class1){
        Optional<Parser<? extends Object>> opParser = PARSERS.stream().filter(p -> p.getDesginedForKeys().isEmpty()).filter(p -> class1.isAssignableFrom(p.getParseType())).findFirst();
        if(opParser.isPresent()){
            return Optional.of((Parser<T>)opParser.get());
        }
        return Optional.empty();
    }

    public static <T extends Object> Optional<Parser<T>> getParser(Key<? extends BaseValue<T>> designedFor){
        Optional<Parser<? extends Object>> opParser = PARSERS.stream().filter(p -> !p.getDesginedForKeys().isEmpty()).filter(p -> p.getDesginedForKeys().contains(designedFor)).findAny();
        if(opParser.isPresent()){
            return Optional.of((Parser<T>)opParser.get());
        }
        return Optional.empty();
    }

    public static <T extends Object> Optional<Parser<T>> getParser(Class<T> class1, Key<? extends BaseValue<T>> designedFor){
        Optional<Parser<T>> opDesignedFor = getParser(designedFor);
        if(opDesignedFor.isPresent()){
            return opDesignedFor;
        }
        return getParser(class1);
    }

    public static void register(Parser<? extends Object>... parser){
        for(Parser<? extends Object> parser1 : parser){
            PARSERS.add(parser1);
        }
    }
}
