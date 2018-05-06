package org.entitydisguise.entitydisguiseplugin.disguise.data.settings.impl;

import org.entitydisguise.entitydisguiseplugin.EntityDisguisePlugin;
import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettings;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettingsData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.ImmutableDisguiseSettingsData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableDisguiseSettingsDataImpl extends AbstractImmutableData<ImmutableDisguiseSettingsData, DisguiseSettingsData> implements ImmutableDisguiseSettingsData {

    private DisguiseSettings settings;

    public ImmutableDisguiseSettingsDataImpl(){
        this(null);
    }

    public ImmutableDisguiseSettingsDataImpl(DisguiseSettings settings){
        this.settings = settings;
        registerGetters();
    }

    public DisguiseSettings getDisguiseSettings(){
        return this.settings;
    }

    @Override
    public ImmutableValue<DisguiseSettings> disguiseSettings(){
        return Sponge.getRegistry().getValueFactory().createValue(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS, this.settings).asImmutable();
    }

    @Override
    public DisguiseSettingsData asMutable() {
        return new DisguiseSettingsDataImpl(this.settings);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    protected void registerGetters(){
        registerKeyValue(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS, this::disguiseSettings);
        registerFieldGetter(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS, () -> this.settings);
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = super.toContainer();
        if(this.settings != null){
            container.set(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS, this.settings);
        }
        return container;
    }
}
