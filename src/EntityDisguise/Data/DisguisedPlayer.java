package EntityDisguise.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;

import EntityDisguise.EntityDisguise;

public class DisguisedPlayer {
	
	Player PLAYER;
	EntityType TYPE;
	Entity DISGUISE;
	
	static Collection<DisguisedPlayer> DISGUISED = new ArrayList<DisguisedPlayer>();
	
	public DisguisedPlayer(Player player){
		PLAYER = player;
		DISGUISED.add(this);
	}
	
	public DisguisedPlayer(Player player, EntityType type) {
		PLAYER = player;
		TYPE = type;
		DISGUISED.add(this);
	}

	public Player getPlayer(){
		return PLAYER;
	}
	
	public Optional<EntityType> getDisguiseType(){
		if (TYPE == null){
			return Optional.empty();
		}else{
			return Optional.of(TYPE);
		}
	}
	
	public void setDisguiseType(EntityType type){
		TYPE = type;
	}

	public Optional<Entity> getDisguise(){
		if (DISGUISE == null){
			return Optional.empty();
		}else{
			return Optional.of(DISGUISE);
		}
	}
	
	public boolean pushDisguise(){
		if (TYPE == null){
			PLAYER.sendMessage(EntityDisguise.getTextFormat("Something went wrong (null TYPE)", true));
			return false;
		}
		if (DISGUISE != null){
			DISGUISE.remove();
		}
		Optional<Entity> oEntity = PLAYER.getWorld().createEntity(TYPE, PLAYER.getLocation().getBlockPosition());
		if (oEntity.isPresent()){
			Entity entity = oEntity.get();
			DISGUISE = entity;
			PLAYER.offer(Keys.INVISIBLE, true);
			if (entity instanceof Living){
				entity.offer(Keys.AI_ENABLED, false);
			}
			PLAYER.getWorld().spawnEntity(oEntity.get(), Cause.source(EntityDisguise.getPlugin().getGame().getRegistry().createBuilder(SpawnCause.Builder.class).type(SpawnTypes.CUSTOM).build()).named("EntityDisguise", this).build());
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
			PLAYER.remove(Keys.INVISIBLE);
			PLAYER.sendMessage(EntityDisguise.getTextFormat("You are no longer disguised", false));
		}
		DISGUISED.remove(this);
		return false;
	}
	
	public boolean updateDisguiseLocation(){
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
		return Optional.empty();
	}
}
