package org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.impl;

import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntity;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.entity.EntitySnapshot;

import java.util.Optional;

public class DisguiseEntityBuilder extends AbstractDataBuilder<DisguiseEntity> {

    public DisguiseEntityBuilder(){
        super(DisguiseEntity.class, 1);
    }

    @Override
    protected Optional<DisguiseEntity> buildContent(DataView container) throws InvalidDataException {
        if(!container.contains(DisguiseEntity.ENTITY_SNAPSHOT)){
            return Optional.empty();
        }
        EntitySnapshot snapshot = container.getObject(DisguiseEntity.ENTITY_SNAPSHOT, EntitySnapshot.class).get();
        return Optional.of(new DisguiseEntity(snapshot));
    }
}
