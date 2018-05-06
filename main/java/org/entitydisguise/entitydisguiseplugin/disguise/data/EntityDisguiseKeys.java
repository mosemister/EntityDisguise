package org.entitydisguise.entitydisguiseplugin.disguise.data;

import com.google.common.reflect.TypeToken;
import org.entitydisguise.entitydisguiseplugin.EntityDisguisePlugin;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntity;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettings;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

public class EntityDisguiseKeys {

    public static Key<Value<DisguiseEntity>> KEY_DISGUISE_ENTITY_SETTINGS;
    public static Key<Value<DisguiseSettings>> KEY_DISGUISE_SETTINGS;

    private static String keyDisguiseEntitySettings = "entity.settings";
    private static String keyDisguiseSettings = "disguise.settings";

    public static void invokeClass(){

    }

    static {
        KEY_DISGUISE_ENTITY_SETTINGS = Key
                .builder()
                .type(new TypeToken<Value<DisguiseEntity>>(){})
                .id(EntityDisguisePlugin.PLUGIN_ID + ":" + keyDisguiseEntitySettings)
                .name("Entity Settings")
                .query(DataQuery.of("entity.settings"))
                .build();
        KEY_DISGUISE_SETTINGS = Key
                .builder()
                .type(new TypeToken<Value<DisguiseSettings>>(){})
                .id(EntityDisguisePlugin.PLUGIN_ID + ":" + keyDisguiseSettings)
                .name("Disguise Settings")
                .query(DataQuery.of("disguise.settings"))
                .build();
    }
}
