package org.entitydisguise.entitydisguiseplugin.disguise.data.settings.impl;

import org.entitydisguise.entitydisguiseplugin.EntityDisguisePlugin;
import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettings;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.entity.EntityType;

import java.util.Optional;

public class DisguiseSettingsBuilder extends AbstractDataBuilder<DisguiseSettings> {

    public static final int CONTENT_VERSION = 2;

    public DisguiseSettingsBuilder(){
        super(DisguiseSettings.class, CONTENT_VERSION);
    }

    @Override
    protected Optional<DisguiseSettings> buildContent(DataView container) throws InvalidDataException {
        if(!container.contains(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS)){
            return Optional.empty();
        }
        double deadzone = container.getObject(DisguiseSettings.DEADZONE_QUERY, Double.class).get();
        return Optional.of(new DisguiseSettings(deadzone));
    }
}
