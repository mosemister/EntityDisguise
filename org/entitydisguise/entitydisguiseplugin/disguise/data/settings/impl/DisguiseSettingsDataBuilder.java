package org.entitydisguise.entitydisguiseplugin.disguise.data.settings.impl;

import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettingsData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.ImmutableDisguiseSettingsData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class DisguiseSettingsDataBuilder extends AbstractDataBuilder<DisguiseSettingsData> implements DataManipulatorBuilder<DisguiseSettingsData, ImmutableDisguiseSettingsData> {

    public DisguiseSettingsDataBuilder(){
        super(DisguiseSettingsData.class, 1);
    }

    @Override
    public DisguiseSettingsDataImpl create() {
        return new DisguiseSettingsDataImpl();
    }

    @Override
    public Optional<DisguiseSettingsData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<DisguiseSettingsData> buildContent(DataView container) throws InvalidDataException {
        return create().from(container);
        /*if(!container.contains(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS)){
            return Optional.empty();
        }
        DisguiseSettingsData data = create();
        container.getSerializable(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS.getQuery(), DisguiseSettings.class).ifPresent(settings -> data.set(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS, settings));
        return Optional.of(data);*/
    }
}
