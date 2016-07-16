package EntityDisguise;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;

public enum Permissions {
	
	CMD_REMOVE("ed.command.remove", "ed.command.*", "ed.*"),
	CMD_DISGUISE_LIST("ed.command.dlist", "ed.command.*", "ed.*"),
	CMD_ENTITY_LIST("ed.command.elist", "ed.command.*", "ed.*");
	
	String[] PERMISSIONS;
	
	private Permissions(String... permissions){
		PERMISSIONS = permissions;
	}
	
	public boolean hasPermission(Player player){
		return Arrays.asList(PERMISSIONS).stream().anyMatch(p -> player.hasPermission(p));
	}
	
	public static boolean hasPermission(Player player, EntityType type){
		if (player.hasPermission("ed.disguise." + type.getName())){
			return true;
		}else if(player.hasPermission("ed.disguise.*")){
			return true;
		}else if(player.hasPermission("ed.*")){
			return true;
		}else{
			return false;
		}
	}
	
	public static List<EntityType> permissionForEntity(Player player){
		Collection<EntityType> entities = EntityDisguise.getTypes();
		return entities.stream().filter(e -> hasPermission(player, e)).collect(Collectors.toList());
	}

}
