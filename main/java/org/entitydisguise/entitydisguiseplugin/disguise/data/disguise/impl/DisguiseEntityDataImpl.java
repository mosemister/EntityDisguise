package org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.impl;

import org.entitydisguise.entitydisguiseplugin.EntityDisguisePlugin;
import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntity;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntityData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.ImmutableDisguiseEntityData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class DisguiseEntityDataImpl extends AbstractData<DisguiseEntityData, ImmutableDisguiseEntityData> implements DisguiseEntityData{

    private DisguiseEntity entity;

    public DisguiseEntityDataImpl(){
        this(new DisguiseEntity());
    }

    public DisguiseEntityDataImpl(DisguiseEntity entity){
        this.entity = entity;
        registerGettersAndSetters();
    }

    public DisguiseEntity getDisguiseEntity(){
        return this.entity;
    }

    public void setDisguiseEntity(DisguiseEntity entity){
        this.entity = entity;
    }

    public Optional<DisguiseEntityData> from(DataView view){
        if(view.contains(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS.getQuery())){
            this.entity = view.getObject(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS.getQuery(), DisguiseEntity.class).get();
            return Optional.of(this);
        }
        return Optional.empty();
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS, () -> this.entity);
        registerFieldSetter(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS, entity -> this.entity = entity);
        registerKeyValue(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS, this::disguiseEntity);
    }

    @Override
    public Value<DisguiseEntity> disguiseEntity() {
        return Sponge.getRegistry().getValueFactory().createValue(EntityDisguiseKeys.KEY_DISGUISE_ENTITY_SETTINGS, this.entity);
    }

    @Override
    public Optional<DisguiseEntityData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<DisguiseEntityData> other = dataHolder.get(DisguiseEntityData.class);
        if(other.isPresent()){
            DisguiseEntityDataImpl data = (DisguiseEntityDataImpl) other.get();
            DisguiseEntityDataImpl finalData = overlap.merge(this, data);
            this.entity = finalData.entity;
        }
        return Optional.of(this);


        /*DisguiseEntityData data = overlap.merge(this, dataHolder.get(DisguiseEntityData.class).orElse(null));
        this.entity = data.disguiseEntity().get();
        return Optional.of(this);*/
    }

    @Override
    public Optional<DisguiseEntityData> from(DataContainer view) {
        return from((DataView)view);
    }

    @Override
    public DisguiseEntityData copy() {
        return new DisguiseEntityDataImpl(this.entity);
    }

    @Override
    public ImmutableDisguiseEntityData asImmutable() {
        return new ImmutableDisguiseEntityDataImpl(this.entity);
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
