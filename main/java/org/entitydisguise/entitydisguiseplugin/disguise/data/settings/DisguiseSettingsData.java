package org.entitydisguise.entitydisguiseplugin.disguise.data.settings;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface DisguiseSettingsData extends DataManipulator<DisguiseSettingsData, ImmutableDisguiseSettingsData> {

    Value<DisguiseSettings> disguiseSettings();
}
