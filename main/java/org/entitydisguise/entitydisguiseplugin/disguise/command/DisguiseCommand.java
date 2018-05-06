package org.entitydisguise.entitydisguiseplugin.disguise.command;

import org.entitydisguise.entitydisguiseplugin.EntityDisguisePlugin;
import org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.EntityKeyArgument;
import org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.EntityKeyValueArgument;
import org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.EntityTypeArgument;
import org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse.Parser;
import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntity;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class DisguiseCommand {

    public static final Text PLAYER = Text.of("player");
    public static final Text ENTITY_TYPE = Text.of("type");
    public static final Text KEY = Text.of("key");
    public static final Text KEY_VALUE = Text.of("key value");

    public static class RemoveDisguiseCommand implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            Player player = args.<Player>getOne(DisguiseCommand.PLAYER).get();
            Optional<DisguiseEntity> opEntity = player.get(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS);
            if(!opEntity.isPresent()){
                throw new CommandException(Text.of("You don't have the DisguiseEntity data put on your player. disconnect and reconnect to fix this."));
            }
            DisguiseEntity entity = opEntity.get();
            player.offer(Keys.VANISH, false);
            player.offer(Keys.VANISH_PREVENTS_TARGETING, false);
            player.offer(Keys.VANISH_IGNORES_COLLISION, false);
            entity.setSnapshot(null);
            return CommandResult.success();
        }
    }

    public static class DisguiseKeysCommand implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            Player player = args.<Player>getOne(DisguiseCommand.PLAYER).get();
            Optional<DisguiseEntity> opDisguise = player.get(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS);
            if(!opDisguise.isPresent()){
                throw new CommandException(Text.of("You don't have the DisguiseEntity data put on your player. disconnect and reconnect to fix this."));
            }
            DisguiseEntity disguise = opDisguise.get();

            Optional<Key<BaseValue<Object>>> opKey = args.getOne(KEY);
            if (!opKey.isPresent()) {
                throw new CommandException(Text.of("Unknown Key"));
            }
            Key<BaseValue<Object>> key = opKey.get();

            Optional<Object> opValue = args.getOne(KEY_VALUE);
            if(!opValue.isPresent()){
                throw new CommandException(Text.of("Unknown Value"));
            }
            Object obj = opValue.get();

            if (disguise.offer(key, obj)) {
                src.sendMessage(Text.of(key.getName() + " has been set for your disguise"));
            }else{
                src.sendMessage(Text.of(key.getName() + " is not supported for your disguise"));
            }
            return CommandResult.success();
        }

    }

    public static class DisguiseTypeCommand implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            Player player = args.<Player>getOne(DisguiseCommand.PLAYER).get();
            EntityType type = null;
            Optional<EntityType> opType = args.getOne(DisguiseCommand.ENTITY_TYPE);
            if(opType.isPresent()){
                type = opType.get();
            }
            Transform<World> loc = player.getTransform();
            Optional<DisguiseEntity> opDisguise = player.get(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS);
            if(!opDisguise.isPresent()){
                throw new CommandException(Text.of("You don't have the DisguiseEntity data put on your player. disconnect and reconnect to fix this."));
            }
            DisguiseEntity disguise = opDisguise.get();
            if(type == null){
                throw new CommandException(Text.of("A entity type currently needs to be specified"));
            }
            if(!disguise.getSnapshot().isPresent() || (disguise.getSnapshot().isPresent() && (!disguise.getSnapshot().get().getType().equals(type)))) {
                EntitySnapshot snapshot = createNewDisguiseSnapshot(player.getTransform(), type);
                disguise.setSnapshot(snapshot);
                Optional<Entity> opEntity = snapshot.restore();
                if(!opEntity.isPresent()){
                    throw new CommandException(Text.of("Unknown Error occurred. Could not restore entity"));
                }
                Entity entity = opEntity.get();
                entity.offer(Keys.AI_ENABLED, false);
                entity.offer(Keys.INVULNERABLE, true);
                disguise.setEntity(entity);
                player.getWorld().spawnEntity(entity);
            }
            Entity entity = disguise.getEntity().get();
            entity.setTransform(loc);
            player.offer(Keys.VANISH, true);
            player.offer(Keys.VANISH_IGNORES_COLLISION, true);
            player.offer(Keys.VANISH_PREVENTS_TARGETING, true);
            disguise.setVisible(true);
            if(src instanceof Player){
                src.sendMessage(Text.of("You have been disguised as a basic " + type.getName() + ". Use '/entitydisguise key <key> <value>' to add more details to your disguise."));
            }else{
                src.sendMessage(Text.join(player.getDisplayNameData().displayName().get(), Text.of(" has been disguised as a basic " + type.getName() + ". Use '/entitydisguise keys <key> <value>' to add more details to your disguise.")));
            }
            return CommandResult.success();
        }
    }

    private static EntitySnapshot createNewDisguiseSnapshot(Transform<World> loc, EntityType type) throws CommandException{
        Entity entity = loc.getExtent().createEntity(type, loc.getPosition());
        if(entity == null){
            throw new CommandException(Text.of("An internal error occurred: Failed to create entity with type " + type.getId()));
        }
        entity.setTransform(loc);
        return entity.createSnapshot();
    }

    public static CommandCallable createCommands(){
        CommandSpec disguise = CommandSpec.builder().executor(new DisguiseCommand.DisguiseTypeCommand()).description(Text.of("disguise as a entity")).permission(EntityDisguisePlugin.PERMISSION_COMMAND_DISGUISE_ENTITY).arguments(GenericArguments.playerOrSource(PLAYER), new EntityTypeArgument(ENTITY_TYPE, false)).build();
        CommandSpec remove = CommandSpec.builder().executor(new DisguiseCommand.RemoveDisguiseCommand()).description(Text.of("remove disguise")).permission(EntityDisguisePlugin.PERMISSION_COMMAND_DISGUISE_ENTITY).arguments(GenericArguments.playerOrSource(PLAYER)).build();
        CommandSpec key = CommandSpec.builder().executor(new DisguiseCommand.DisguiseKeysCommand()).description(Text.of("add detail to disguise")).permission(EntityDisguisePlugin.PERMISSION_COMMAND_DISGUISE_KEY).arguments(GenericArguments.playerOrSource(PLAYER), new EntityKeyArgument(KEY), new EntityKeyValueArgument(KEY_VALUE)).build();
        return CommandSpec.builder().child(key, "disguisekey", "key").child(remove, "removedisguise", "remove").child(disguise, "disguiseas", "as").permission(EntityDisguisePlugin.PERMISSION_COMMAND_ALL).description(Text.of("Entity Disguise Commands")).build();
    }
}
