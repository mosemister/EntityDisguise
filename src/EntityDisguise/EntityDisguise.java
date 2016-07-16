package EntityDisguise;

import java.util.Collection;
import java.util.Optional;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import EntityDisguise.Listeners.Listeners;
import EntityDisguise.Listeners.Commands.EntityDisguiseCommand;

import com.google.inject.Inject;

@Plugin(id = EntityDisguise.ID, name = EntityDisguise.NAME, version = EntityDisguise.VERSION)
public class EntityDisguise {
	
	public static final String ID = "edisguise";
	public static final String NAME = "EntityDisguise";
	public static final String VERSION = "0.5 Alpha";
	
	@Inject
	Game GAME;
	
	static EntityDisguise PLUGIN;
	
	@Listener
	public void onEnable(GameStartedServerEvent event){
		System.out.println("Entity Disguise is booting up");
		PLUGIN = this;
		
		EntityDisguiseCommand command2 = new EntityDisguiseCommand();
		
		GAME.getCommandManager().register(this, command2, "EntityDisguise", "Ed");
		GAME.getEventManager().registerListeners(this, new Listeners());
	}
	
	public Game getGame(){
		return GAME;
	}
	
	public static EntityDisguise getPlugin(){
		return PLUGIN;
	}
	
	public static Text getTextFormat(String message, boolean error){
		if (error){
			return Text.builder("[EDisguise]").color(TextColors.GOLD).append(Text.builder(message).color(TextColors.RED).build()).build();
		}else{
			return Text.builder("[EDisguise]").color(TextColors.GOLD).append(Text.builder(message).color(TextColors.AQUA).build()).build();
		}
	}
	
	public static Collection<EntityType> getTypes(){
		Collection<EntityType> entities = getPlugin().getGame().getRegistry().getAllOf(EntityType.class);
		return entities;
	}
	
	public static Optional<EntityType> getType(String name){
		for (EntityType type : getTypes()){
			if (type.getName().equalsIgnoreCase(name)){
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}

}
