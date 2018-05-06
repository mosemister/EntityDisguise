package org.entitydisguise.entitydisguiseplugin.disguise.listeners;

import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntity;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntityData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettingsData;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.filter.data.Has;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Optional;

public class Listeners {

    @Listener
    public void onPlayerMove(MoveEntityEvent event, @Root Player player){
        Optional<DisguiseEntity> opDisgEntity = player.get(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS);
        if(!opDisgEntity.isPresent()){
            return;
        }
        DisguiseEntity disgEntity = opDisgEntity.get();
        disgEntity.safeUpdate(player);
    }

    @Listener
    public void onPlayerJoinDisguiseSettings(ClientConnectionEvent.Join event, @Getter("getTargetEntity") @Has(value = DisguiseSettingsData.class, inverse = true) Player player){
        player.offer(player.getOrCreate(DisguiseSettingsData.class).get());
    }

    @Listener
    public void onPlayerJoinDisguiseEntity(ClientConnectionEvent.Join event, @Getter("getTargetEntity") @Has(value = DisguiseEntityData.class, inverse = true) Player player){
        player.offer(player.getOrCreate(DisguiseEntityData.class).get());
        DisguiseEntity disguise = player.get(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS).get();
        if(!disguise.getSnapshot().isPresent()){
            return;
        }
        Optional<Entity> opEntity = disguise.getSnapshot().get().restore();
        if(!opEntity.isPresent()){
            return;
        }
        Entity entity = opEntity.get();
        disguise.setEntity(entity);
    }

    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") @Has(value = DisguiseEntityData.class) Player player){
        DisguiseEntity disguise = player.get(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS).get();
        disguise.destroy();
        player.offer(Keys.VANISH, false);
        player.offer(Keys.VANISH_PREVENTS_TARGETING, false);
        player.offer(Keys.VANISH_IGNORES_COLLISION, false);
    }
}
