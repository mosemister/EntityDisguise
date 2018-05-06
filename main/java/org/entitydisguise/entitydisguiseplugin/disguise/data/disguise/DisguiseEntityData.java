package org.entitydisguise.entitydisguiseplugin.disguise.data.disguise;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface DisguiseEntityData extends DataManipulator<DisguiseEntityData, ImmutableDisguiseEntityData> {

    public Value<DisguiseEntity> disguiseEntity();
}
