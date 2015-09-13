package EntityDisguise.Listeners.Commands;

import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.source.ConsoleSource;

import com.google.common.base.Optional;

import EntityDisguise.EntityDisguise;
import EntityDisguise.Data.CreatureTypes;
import EntityDisguise.Data.DisguisedPlayer;
import EntityDisguise.Listeners.Commands.Interface.EDCommand;

public class EntityDisguiseCommand extends EDCommand{

	@Override
	public boolean runPlayerCommand(Player player, String[] args) {
		if (args.length >1){
			if (args[0].equalsIgnoreCase("disguise")){
				if (args.length >= 2){
					EntityType type = CreatureTypes.getType(args[1]);
					if (type == null){
						player.sendMessage(EntityDisguise.getTextFormat("EntityType is currently not supported", false));
						return false;
					}else{
						if (player.hasPermission("entitydisguise.disguise." + type.getName().toLowerCase()) || hasPermission(player)){
							disguise(player, type);
							return true;
						}else{
							player.sendMessage(EntityDisguise.getTextFormat("Permission missmatch", true));
						}
					}
				}
			}else if (args[0].equalsIgnoreCase("remove")){
				remove(player);
			}else if (args[0].equalsIgnoreCase("EList")){
				listEntity(player);
			}else if (args[0].equalsIgnoreCase("DList")){
				listDisguised(player);
			}
		}else{
			displayHelp(player);
		}
		return false;
	}
	
	void disguise(Player player, EntityType type){
		Optional<DisguisedPlayer> disguised = DisguisedPlayer.getDisguise(player);
		if (disguised.isPresent()){
			disguised.get().setDisguiseType(type);
			disguised.get().updateDisguise();
		}else{
			DisguisedPlayer disguise = new DisguisedPlayer(player, type);
			disguise.pushDisguise();
		}
	}
	
	void remove(Player player){
		Optional<DisguisedPlayer> disguised = DisguisedPlayer.getDisguise(player);
		if (disguised.isPresent()){
			disguised.get().stopDisguise();
		}else{
			player.sendMessage(EntityDisguise.getTextFormat("", false));
			return;
		}
	}
	
	void listDisguised(Player player){
		for (DisguisedPlayer player2 : DisguisedPlayer.getDisguises()){
			player.sendMessage(Texts.builder(player2.getPlayer().getName()).color(TextColors.AQUA).build());
		}
	}
	
	void listEntity(Player player){
		for(CreatureTypes type : CreatureTypes.values()){
			player.sendMessage(Texts.builder(type.getType().getName()).color(TextColors.AQUA).build());
		}
	}
	
	void displayHelp(Player player){
		player.sendMessage(Texts.builder("/ED disguise <EntityType>").color(TextColors.GOLD).append(Texts.builder(": disguises you as the entity").color(TextColors.AQUA).build()).build());
		player.sendMessage(Texts.builder("/ED remove <EntityType>").color(TextColors.GOLD).append(Texts.builder(": removes the disguise").color(TextColors.AQUA).build()).build());
		player.sendMessage(Texts.builder("/ED EList").color(TextColors.GOLD).append(Texts.builder(": List the EntityTypes allowed").color(TextColors.AQUA).build()).build());
		player.sendMessage(Texts.builder("/ED DList").color(TextColors.GOLD).append(Texts.builder(": List the disguised players").color(TextColors.AQUA).build()).build());
	}

	@Override
	public boolean hasPermission(Player player) {
		return player.hasPermission("EntityDisguise.*");
	}

	@Override
	public boolean runConsoleCommand(ConsoleSource sender, String[] args) {
		// TODO Console commands
		return false;
	}

	@Override
	public String getCommandName() {
		//short for EntityDisguise, didn't think of the shorter version ......... shout out to all Ed's in this world
		return "ED";
	}

	@Override
	public String getUsage() {
		return "/ED";
	}

}
