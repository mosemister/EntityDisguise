package org.entitydisguise.entitydisguiseplugin.disguise.data.settings;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutableDisguiseSettingsData extends ImmutableDataManipulator<ImmutableDisguiseSettingsData, DisguiseSettingsData>{

    public ImmutableValue<DisguiseSettings> disguiseSettings();
}
