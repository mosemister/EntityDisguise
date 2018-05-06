package org.entitydisguise.entitydisguiseplugin.disguise;

import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;

import java.util.Arrays;
import java.util.List;

public interface EntityDisguiseInformation {

    List<EntityType> RECOMMENDED_ENTITIES = Arrays.asList(EntityTypes.ZOMBIE, EntityTypes.HUMAN, EntityTypes.BLAZE, EntityTypes.PARROT);
}
