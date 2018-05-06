package org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.impl;

import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntityData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.ImmutableDisguiseEntityData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class DisguiseEntityDataBuilder extends AbstractDataBuilder<DisguiseEntityData> implements DataManipulatorBuilder<DisguiseEntityData, ImmutableDisguiseEntityData> {

    public DisguiseEntityDataBuilder() {
        super(DisguiseEntityData.class, 1);
    }

    @Override
    public DisguiseEntityDataImpl create() {
        return new DisguiseEntityDataImpl();
    }

    @Override
    public Optional<DisguiseEntityData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<DisguiseEntityData> buildContent(DataView container) throws InvalidDataException {
        return create().from(container);
        /*if(!container.contains(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS)){
            return Optional.empty();
        }
        DisguiseEntityData data = new DisguiseEntityDataImpl();
        container.getSerializable(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS.getQuery(), DisguiseEntity.class).ifPresent(disg -> data.set(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS, disg));
        return Optional.of(data);*/
    }
}
