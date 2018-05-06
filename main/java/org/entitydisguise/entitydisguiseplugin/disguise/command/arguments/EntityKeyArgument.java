package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments;

import com.google.common.reflect.TypeToken;
import org.entitydisguise.entitydisguiseplugin.EntityDisguisePlugin;
import org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse.Parser;
import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntity;
import org.entitydisguise.entitydisguiseplugin.disguise.data.entry.BasicEntry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.*;

public class EntityKeyArgument extends CommandElement {

    public EntityKeyArgument(Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected Key<BaseValue<? extends Object>> parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        DisguiseEntity disguise = getDisguise(source, args);
        String keyString = args.next();
        Optional<Key<BaseValue<? extends Object>>> opKey = parseKey(keyString, disguise);
        if(!opKey.isPresent()){
            throw args.createError(Text.of("Unknown key"));
        }
        Key<BaseValue<? extends Object>> key = opKey.get();
        return key;

    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        List<String> suggested = new ArrayList<>();
        try {
            DisguiseEntity disguise = getDisguise(src, args);
            String peek = args.peek();
            getSupportedKeys(disguise).stream()
                    .filter(k -> k.getId().toLowerCase().contains(peek.toLowerCase()))
                    .forEach(k -> suggested.add(k.getId()));
        } catch (ArgumentParseException e) {
        }
        return suggested;
    }

    private Optional<Key<BaseValue<? extends Object>>> parseKey(String key, DisguiseEntity entity){
        return getSupportedKeys(entity).stream().filter(k -> k.getId().equalsIgnoreCase(key) || k.getName().equalsIgnoreCase(key)).findFirst();
    }

    private List<Key<BaseValue<? extends Object>>> getSupportedKeys(DisguiseEntity entity){
        List<Key<BaseValue<? extends Object>>> list = new ArrayList<>();
        Sponge.getRegistry().getAllOf(Key.class).stream()
                .filter(k -> {
                    Key<BaseValue<? extends Object>> key = (Key<BaseValue<? extends Object>>) k;
                    return entity.supports(key);
                }).forEach(k -> list.add((Key<BaseValue<? extends Object>>) k));
        return list;
    }

    private DisguiseEntity getDisguise(CommandSource source, CommandArgs arg) throws ArgumentParseException{
            if(source instanceof Player){
                Player player = (Player)source;
                Optional<DisguiseEntity> opDisguise = player.get(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS);
                if(opDisguise.isPresent()){
                    return opDisguise.get();
                }
                throw arg.createError(Text.of("No disguise found"));
            }
            throw arg.createError(Text.of("Player required"));
    }
}
