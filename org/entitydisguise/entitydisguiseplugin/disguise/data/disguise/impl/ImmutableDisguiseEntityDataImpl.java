package org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.impl;

import org.entitydisguise.entitydisguiseplugin.EntityDisguisePlugin;
import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntity;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntityData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.ImmutableDisguiseEntityData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableDisguiseEntityDataImpl extends AbstractImmutableData<ImmutableDisguiseEntityData, DisguiseEntityData> implements ImmutableDisguiseEntityData {

    private final DisguiseEntity entity;

    public ImmutableDisguiseEntityDataImpl(){
        this(null);
    }

    public ImmutableDisguiseEntityDataImpl(DisguiseEntity entity){
        this.entity = entity;
        registerGetters();
    }

    public DisguiseEntity getDisguiseEntity(){
        return entity;
    }

    @Override
    public ImmutableValue<DisguiseEntity> disguiseEntity() {
        return Sponge.getRegistry().getValueFactory().createValue(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS, this.entity).asImmutable();
    }

    @Override
    public DisguiseEntityData asMutable() {
        return new DisguiseEntityDataImpl(entity);
    }

    @Override
    protected void registerGetters() {
        registerKeyValue(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS, this::disguiseEntity);
        registerFieldGetter(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS, () -> this.entity);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }


    @Override
    public DataContainer toContainer(){
        DataContainer container = super.toContainer();
        /*if(this.entity != null){
            return container.set(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS, this.entity);
        }*/
        return container;
    }
}
