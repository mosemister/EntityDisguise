package org.entitydisguise.entitydisguiseplugin.disguise.data.disguise;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutableDisguiseEntityData extends ImmutableDataManipulator<ImmutableDisguiseEntityData, DisguiseEntityData> {

    ImmutableValue<DisguiseEntity> disguiseEntity();
}
