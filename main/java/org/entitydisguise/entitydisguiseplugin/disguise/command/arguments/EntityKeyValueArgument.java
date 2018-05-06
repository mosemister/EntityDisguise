package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments;

import org.entitydisguise.entitydisguiseplugin.EntityDisguisePlugin;
import org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse.Parser;
import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntity;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityKeyValueArgument extends CommandElement {

    public EntityKeyValueArgument(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String keyString = args.getAll().get(args.getAll().size() - 2);
        Optional<Key<BaseValue<Object>>> opKey = parseKey(keyString);
        if(!opKey.isPresent()){
            throw args.createError(Text.of("Unknown Key"));
        }
        Key<? extends BaseValue<Object>> key = opKey.get();
        Parser<Object> parser;
        try {
            parser = getParser(source, args, key);
        } catch (Exception e) {
            throw args.createError(Text.of(e.getMessage()));
        }
        Object parsed;
        try {
            parsed = parser.parse(args.next());
        } catch (CommandException e) {
            throw args.createError(e.getText());
        }
        return parsed;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        String keyString = args.getAll().get(args.getRawPosition() - 2);
        Optional<Key<BaseValue<Object>>> opKey = parseKey(keyString);
        if(!opKey.isPresent()){
            return new ArrayList<>();
        }
        Key<? extends BaseValue<Object>> key = opKey.get();
        Parser<Object> parser;
        try {
            parser = getParser(src, args, key);
        } catch (Exception e) {
            return new ArrayList<>();
        }
        try {
            return parser.getSuggested(args.peek());
        } catch (ArgumentParseException e) {
            return new ArrayList<>();
        }
    }

    private Optional<Key<BaseValue<Object>>> parseKey(String key){
        List<Key<BaseValue<Object>>> list = new ArrayList<>();
        Sponge.getRegistry().getAllOf(Key.class).stream().forEach(k -> list.add((Key<BaseValue<Object>>)k));
        return list.stream().filter(k -> k.getId().equalsIgnoreCase(key) || k.getName().equalsIgnoreCase(key)).findFirst();
    }

    private <T extends Object> Parser<T> getParser(CommandSource src, CommandArgs args, Key<? extends BaseValue<T>> key) throws Exception{
        DisguiseEntity disguise;
        try {
            disguise = getDisguise(src, args);
        } catch (ArgumentParseException e) {
            throw new Exception("No Disguise Found");
        }
        if(!disguise.getEntity().isPresent()){
            throw new Exception("No Disguise Found");
        }
        Class<T> class1 = EntityDisguisePlugin.getTypeFromKey(disguise.getEntity().get(), key);
        Optional<Parser<T>> opParser = Parser.getParser(class1, key);
        if(opParser.isPresent()){
            return opParser.get();
        }
        throw new Exception("No Support For " + class1.getSimpleName());
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
