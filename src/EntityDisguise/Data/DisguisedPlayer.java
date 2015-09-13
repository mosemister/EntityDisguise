package EntityDisguise.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;

import EntityDisguise.EntityDisguise;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

public class DisguisedPlayer {
	
	Player PLAYER;
	EntityType TYPE;
	Entity DISGUISE;
	
	static Collection<DisguisedPlayer> DISGUISED = new ArrayList<DisguisedPlayer>();
	
	public DisguisedPlayer(Player player){
		PLAYER = player;
	}
	
	public DisguisedPlayer(Player player, EntityType type) {
		PLAYER = player;
		TYPE = type;
	}

	public Player getPlayer(){
		return PLAYER;
	}
	
	public Optional<EntityType> getDisguiseType(){
		if (TYPE == null){
			return Optional.absent();
		}else{
			return Optional.of(TYPE);
		}
	}
	
	public void setDisguiseType(EntityType type){
		TYPE = type;
	}

	public Optional<Entity> getDisguise(){
		if (DISGUISE == null){
			return Optional.absent();
		}else{
			return Optional.of(DISGUISE);
		}
	}
	
	public boolean pushDisguise(){
		if (TYPE != null){
			return false;
		}
		if (DISGUISE != null){
			DISGUISE.remove();
		}
		Optional<Entity> oEntity = PLAYER.getWorld().createEntity(TYPE, PLAYER.getLocation().getBlockPosition());
		if (oEntity.isPresent()){
			Entity entity = oEntity.get();
			DISGUISE = entity;
			Set<UUID> data = Sets.newConcurrentHashSet();
			for(Player player : EntityDisguise.getPlugin().getGame().getServer().getOnlinePlayers()){
				if (player != PLAYER){
					data.add(player.getUniqueId());
				}
			}
			/*PLAYER.offer(Keys.INVISIBLE_TO_PLAYER_IDS, data);
			Set<UUID> data2 = Sets.newConcurrentHashSet();
			data2.add(PLAYER.getUniqueId());
			DISGUISE.offer(Keys.INVISIBLE_TO_PLAYER_IDS, data2)*/;
			PLAYER.sendMessage(EntityDisguise.getTextFormat("You are now disguised as a " + TYPE.getName(), false));
			return true;
		}
		PLAYER.sendMessage(EntityDisguise.getTextFormat("Something went wrong (failed on push).", true));
		return false;
	}
	
	public boolean stopDisguise(){
		if (DISGUISE != null){
			DISGUISE.remove();
			DISGUISE = null;
			PLAYER.remove(Keys.INVISIBLE_TO_PLAYER_IDS);
			PLAYER.sendMessage(EntityDisguise.getTextFormat("You are no longer disguised", false));
		}
		return false;
	}
	
	public boolean updateDisguise(){
		if (DISGUISE != null){
			DISGUISE.setLocationSafely(PLAYER.getLocation());
			DISGUISE.setRotation(PLAYER.getRotation());
		}
		return false;
	}
	
	public static Collection<DisguisedPlayer> getDisguises(){
		return DISGUISED;
	}
	
	public static Optional<DisguisedPlayer> getDisguise(Player player){
		for(DisguisedPlayer dis : getDisguises()){
			if (dis.getPlayer().equals(player)){
				return Optional.of(dis);
			}
		}
		return Optional.absent();
	}
}
