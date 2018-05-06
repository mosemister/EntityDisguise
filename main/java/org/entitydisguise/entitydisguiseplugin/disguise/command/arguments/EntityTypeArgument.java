package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments;

import org.entitydisguise.entitydisguiseplugin.disguise.EntityDisguiseInformation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class EntityTypeArgument extends CommandElement {

    boolean recommended;

    public EntityTypeArgument(Text key){
        this(key, false);
    }

    public EntityTypeArgument(Text key, boolean useRecommended) {
        super(key);
        this.recommended = useRecommended;
    }

    @Nullable
    @Override
    protected EntityType parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String entityTypeString = args.next();
        List<EntityType> types = getTypes();
        Optional<EntityType> opType = types.stream().filter(e -> (e.getName().equalsIgnoreCase(entityTypeString)) || (e.getId().equalsIgnoreCase(entityTypeString))).findFirst();
        if(opType.isPresent()){
            return opType.get();
        }
        throw args.createError(Text.of("Cannot find Entity of " + entityTypeString));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        List<EntityType> types = getTypes();
        List<String> ret1 = new ArrayList<>();
        try {
            String entityTypeString = args.peek();
            types.stream().filter(e -> entityTypeString.startsWith(e.getId())).forEach(e -> ret1.add(e.getId()));
            types.stream().filter(e -> e.getId().contains(entityTypeString)).filter(e -> !ret1.stream().anyMatch(t -> t.equals(e.getId()))).forEach(e -> ret1.add(e.getId()));
        } catch (ArgumentParseException e) {
        }
        return ret1;
    }

    private List<EntityType> getTypes(){
        List<EntityType> types = new ArrayList<>(EntityDisguiseInformation.RECOMMENDED_ENTITIES);
        if(!recommended){
            return new ArrayList<>(Sponge.getRegistry().getAllOf(EntityType.class));
        }
        return types;
    }
}
