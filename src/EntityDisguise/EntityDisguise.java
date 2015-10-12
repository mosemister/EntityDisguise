package EntityDisguise;

import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import EntityDisguise.Listeners.Listeners;
import EntityDisguise.Listeners.Commands.EntityDisguiseCommand;

import com.google.inject.Inject;

@Plugin(id = "EDisguise", name = "EntityDisguise")
public class EntityDisguise {
	
	@Inject
	Game GAME;
	
	static EntityDisguise PLUGIN;
	
	@Listener
	public void onEnable(GameStartingServerEvent event){
		PLUGIN = this;
		GAME.getCommandDispatcher().register(this, new EntityDisguiseCommand(), "EntityDisguise", "ED");
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
			return Texts.builder("[EDisguise]").color(TextColors.GOLD).append(Texts.builder(message).color(TextColors.RED).build()).build();
		}else{
			return Texts.builder("[EDisguise]").color(TextColors.GOLD).append(Texts.builder(message).color(TextColors.AQUA).build()).build();
		}
	}

}
