package org.entitydisguise.entitydisguiseplugin;

import com.google.common.reflect.TypeToken;
import org.entitydisguise.entitydisguiseplugin.disguise.command.DisguiseCommand;
import org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse.*;
import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntity;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.DisguiseEntityData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.ImmutableDisguiseEntityData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.impl.DisguiseEntityBuilder;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.impl.DisguiseEntityDataBuilder;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.impl.DisguiseEntityDataImpl;
import org.entitydisguise.entitydisguiseplugin.disguise.data.disguise.impl.ImmutableDisguiseEntityDataImpl;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettings;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettingsData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.ImmutableDisguiseSettingsData;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.impl.DisguiseSettingsDataBuilder;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.impl.DisguiseSettingsDataImpl;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.impl.ImmutableDisguiseSettingsDataImpl;
import org.entitydisguise.entitydisguiseplugin.disguise.listeners.Listeners;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;


@Plugin(id = EntityDisguisePlugin.PLUGIN_ID,
        name = EntityDisguisePlugin.PLUGIN_NAME,
        version = EntityDisguisePlugin.PLUGIN_VERSION,
        //authors = EntityDisguisePlugin.PLUGIN_AUTHORS,
        description = EntityDisguisePlugin.PLUGIN_DESCRIPTION)
public class EntityDisguisePlugin {

    public static final String PLUGIN_ID = "entitydisguiseplugin";
    public static final String PLUGIN_NAME = "Entity Disguise";
    public static final String PLUGIN_VERSION = "SNAPSHOT-02";
    public static final String PLUGIN_DESCRIPTION = "Disguise into any mob";

    public static final String PERMISSION_COMMAND_DISGUISE_ENTITY = "entitydisguise.cmd.disguise";
    public static final String PERMISSION_COMMAND_DISGUISE_KEY = "entitydisguise.cmd.key";
    public static final String PERMISSION_COMMAND_ALL = "entitydisguise.cmd";

    private static String keyDisguiseEntitySettings = "entity.settings";
    private static String keyDisguiseSettings = "disguise.settings";

    @Inject
    private PluginContainer container;

    @Listener
    public void preInit(GamePreInitializationEvent event){
        EntityDisguiseKeys.invokeClass();
        DataRegistration.builder()
                .dataName("Entity Settings")
                .manipulatorId(keyDisguiseEntitySettings)
                .dataClass(DisguiseEntityData.class)
                .immutableClass(ImmutableDisguiseEntityData.class)
                .builder(new DisguiseEntityDataBuilder())
                .dataImplementation(DisguiseEntityDataImpl.class)
                .immutableImplementation(ImmutableDisguiseEntityDataImpl.class)
                .buildAndRegister(this.container);
        DataRegistration.builder()
                .dataName("Disguise Settings")
                .manipulatorId(keyDisguiseSettings)
                .dataClass(DisguiseSettingsData.class)
                .immutableClass(ImmutableDisguiseSettingsData.class)
                .builder(new DisguiseSettingsDataBuilder())
                .dataImplementation(DisguiseSettingsDataImpl.class)
                .immutableImplementation(ImmutableDisguiseSettingsDataImpl.class)
                .buildAndRegister(this.container);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        registerCommands();
        registerParsers();

        registerListeners();
    }

    private void registerCommands(){
        Sponge.getCommandManager().register(this, DisguiseCommand.createCommands(), "entitydisguise", "disguise", "ed");
    }

    private void registerParsers(){
        Parser.register(new BooleanParser(), new DoubleParse(), new IntegerParser(), new StringParser(), new TextParser(), new UUIDParser());
    }

    private void registerListeners(){
        Sponge.getEventManager().registerListeners(this, new Listeners());
    }

    public static <T extends Object> Class<T> getTypeFromKey(ValueContainer<? extends Object> container, Key<? extends BaseValue<T>> key) throws Exception{
        Optional<T> opCurrent = container.get(key);
        Class<T> currentClass;
        if(opCurrent.isPresent()) {
            T value = opCurrent.get();
            currentClass = (Class<T>) value.getClass();
        }else{
            currentClass = getDefaultClass(key);
            if(currentClass == null) {
                if (key.getId().startsWith("sponge:")) {
                    throw new Exception("Unknown default value. Please specify this to the developer of EntityDisguise for a fix");
                } else {
                    throw new Exception("Unknown default value.");
                }
            }
        }
        return currentClass;
    }

    private static <T extends Object> Class<T> getDefaultClass(Key<? extends BaseValue<T>> key){
        if(key.getId().equals(Keys.DISPLAY_NAME.getId())){
            return (Class<T>)LiteralText.class;
        }
        if(key.getId().equals(Keys.FIRE_TICKS.getId())){
            return (Class<T>)Integer.class;
        }
        if(key.getId().equals(Keys.SKIN_UNIQUE_ID.getId())){
            return (Class<T>)UUID.class;
        }
        return null;
    }
}
