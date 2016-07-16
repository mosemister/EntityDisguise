package EntityDisguise.Listeners.Commands;

import java.util.Optional;

import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import EntityDisguise.Permissions;
import EntityDisguise.EntityDisguise;
import EntityDisguise.Data.DisguisedPlayer;
import EntityDisguise.Listeners.Commands.Interface.EDCommand;

public class EntityDisguiseCommand extends EDCommand {

	@Override
	public boolean runPlayerCommand(Player player, String[] args) {
		if (args[0].equalsIgnoreCase("disguise")) {
			if (args.length >= 2) {
				Optional<EntityType> type = EntityDisguise.getType(args[1]);
				if (!type.isPresent()) {
					player.sendMessage(EntityDisguise.getTextFormat("Can not find entity type " + args[1], false));
					return false;
				} else {
					if (player.hasPermission("entitydisguise.disguise." + type.get().getName().toLowerCase()) || hasPermission(player)) {
						disguise(player, type.get());
						return true;
					} else {
						player.sendMessage(EntityDisguise.getTextFormat("Permission missmatch", true));
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("remove")) {
			remove(player);
			return true;
		} else if ((args[0].equalsIgnoreCase("EList")) || (args[0].equalsIgnoreCase("mobs"))) {
			listEntity(player);
			return true;
		} else if (args[0].equalsIgnoreCase("DList")) {
			listDisguised(player);
			return true;
		}
		displayHelp(player);
		return false;
	}

	void disguise(Player player, EntityType type) {
		if (Permissions.hasPermission(player, type)) {
			Optional<DisguisedPlayer> disguised = DisguisedPlayer.getDisguise(player);
			if (disguised.isPresent()) {
				if(disguised.get().setDisguiseType(type)){
					disguised.get().pushDisguise();
				}else{
					player.sendMessage(Text.builder("No permission").build());
				}
			} else {
				DisguisedPlayer disguise = new DisguisedPlayer(player, type);
				disguise.pushDisguise();
			}
		}else{
			player.sendMessage(Text.builder("No permission").build());
		}
	}

	void remove(Player player) {
		if (Permissions.CMD_REMOVE.hasPermission(player)) {
			Optional<DisguisedPlayer> disguised = DisguisedPlayer.getDisguise(player);
			if (disguised.isPresent()) {
				disguised.get().stopDisguise();
			} else {
				player.sendMessage(EntityDisguise.getTextFormat("", false));
				return;
			}
		}else{
			player.sendMessage(Text.builder("No permission").build());
		}
	}

	void listDisguised(Player player) {
		if (Permissions.CMD_DISGUISE_LIST.hasPermission(player)) {
			for (DisguisedPlayer player2 : DisguisedPlayer.getDisguises()) {
				player.sendMessage(Text.builder(player2.getPlayer().getName()).color(TextColors.AQUA).build());
			}
		}else{
			player.sendMessage(Text.builder("No permission").build());
		}
	}

	void listEntity(Player player) {
		if (Permissions.CMD_ENTITY_LIST.hasPermission(player)) {
			String mobs = null;
			for (EntityType type : Permissions.permissionForEntity(player)) {
				if(mobs == null){
					mobs = type.getName();
				}else{
					mobs = mobs + ", " + type.getName();
				}
			}
			player.sendMessage(Text.builder(mobs).color(TextColors.AQUA).build());
		}else{
			player.sendMessage(Text.builder("No permission").build());
		}
	}

	void displayHelp(Player player) {
		player.sendMessage(Text.builder("/ED disguise <EntityType>").color(TextColors.GOLD).append(Text.builder(": disguises you as the entity").color(TextColors.AQUA).build()).build());
		player.sendMessage(Text.builder("/ED remove <EntityType>").color(TextColors.GOLD).append(Text.builder(": removes the disguise").color(TextColors.AQUA).build()).build());
		player.sendMessage(Text.builder("/ED EList").color(TextColors.GOLD).append(Text.builder(": List the EntityTypes allowed").color(TextColors.AQUA).build()).build());
		player.sendMessage(Text.builder("/ED DList").color(TextColors.GOLD).append(Text.builder(": List the disguised players").color(TextColors.AQUA).build()).build());
	}

	@Override
	public boolean hasPermission(Player player) {
		// return player.hasPermission("EntityDisguise.*");
		return true;
	}

	@Override
	public boolean runConsoleCommand(ConsoleSource sender, String[] args) {
		return false;
	}

	@Override
	public String getCommandName() {
		// short for EntityDisguise, didn't think of the shorter version
		// ......... shout out to all Ed's in this world
		return "ED";
	}

	@Override
	public String getUsage() {
		return "/ED";
	}

}
