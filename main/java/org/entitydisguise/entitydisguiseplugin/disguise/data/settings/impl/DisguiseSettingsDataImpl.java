package org.entitydisguise.entitydisguiseplugin.disguise.data.settings.impl;

import org.entitydisguise.entitydisguiseplugin.EntityDisguisePlugin;
import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettings;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettingsData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.ImmutableDisguiseSettingsData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class DisguiseSettingsDataImpl extends AbstractData<DisguiseSettingsData, ImmutableDisguiseSettingsData> implements DisguiseSettingsData {

    private DisguiseSettings settings;

    public DisguiseSettingsDataImpl(){
        this(new DisguiseSettings(2));
    }

    public DisguiseSettingsDataImpl(DisguiseSettings settings){
        this.settings = settings;
        registerGettersAndSetters();
    }

    public void setSettings(DisguiseSettings settings){
        this.settings = settings;
    }

    public DisguiseSettings getDisguiseSettings(){
        return this.settings;
    }

    public Optional<DisguiseSettingsData> from(DataView view){
        if(view.contains(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS.getQuery())){
            this.settings = view.getObject(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS.getQuery(), DisguiseSettings.class).get();
            return Optional.of(this);
        }
        return Optional.empty();
    }

    @Override
    public Value<DisguiseSettings> disguiseSettings() {
        return Sponge.getRegistry().getValueFactory().createValue(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS, this.settings);
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS, () -> this.settings);
        registerFieldSetter(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS, settings -> this.settings = settings);
        registerKeyValue(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS, this::disguiseSettings);
    }

    @Override
    public Optional<DisguiseSettingsData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<DisguiseSettingsData> opData = dataHolder.get(DisguiseSettingsData.class);
        if(opData.isPresent()){
            DisguiseSettingsData data = opData.get();
            final DisguiseSettingsData finalData = overlap.merge(this, data);
            this.settings = finalData.disguiseSettings().get();
        }
        return Optional.of(this);
    }

    @Override
    public Optional<DisguiseSettingsData> from(DataContainer container) {
        return from((DataView)container);
        /*if(!container.contains(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS)){
            return Optional.empty();
        }
        this.settings = container.getSerializable(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS.getQuery(), DisguiseSettings.class).get();
        return Optional.of(this);*/
    }

    @Override
    public DisguiseSettingsData copy() {
        return new DisguiseSettingsDataImpl(this.settings);
    }

    @Override
    public ImmutableDisguiseSettingsDataImpl asImmutable() {
        return new ImmutableDisguiseSettingsDataImpl(this.settings);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = super.toContainer();
        if(this.settings != null){
            return container.set(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS.getQuery(), this.settings);
        }
        return container;
    }
}
