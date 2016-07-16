package EntityDisguise.Listeners;

import java.util.Optional;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;

import EntityDisguise.Data.DisguisedPlayer;

public class Listeners {
	
	/*@Listener
	public void onEntityCollide(CollideEntityEvent event){
		List<Entity> entities = event.getEntities().stream().filter(e -> (e instanceof Player)).collect(Collectors.toList());
		for(Entity entity : entities){
			Player player = (Player)entity;
			Optional<DisguisedPlayer> opDP = DisguisedPlayer.getDisguise(player);
			if(opDP.isPresent()){
				DisguisedPlayer dPlayer = opDP.get();
				Optional<Entity> opDisguise = dPlayer.getDisguise();
				if(opDisguise.isPresent()){
					Entity disguise = opDisguise.get();
				}
			}
		}
	}*/
	
	@Listener
	public void onEntityDamage(DamageEntityEvent event){
		Entity entity = event.getTargetEntity();
		for(int A = 0; A < DisguisedPlayer.getDisguises().size(); A++){
			DisguisedPlayer player = DisguisedPlayer.getDisguises().get(A);
			if (player.getDisguise().isPresent()){
				if (!event.getCause().equals(player.getPlayer())){
					if (player.getDisguise().get().equals(entity)){
						player.stopDisguise();
						HealthData data = player.getPlayer().getHealthData();
						player.getPlayer().offer(Keys.HEALTH, data.health().get() - event.getBaseDamage());
						event.setBaseDamage(0);
					}
				}else{
					event.setBaseDamage(0);
				}
			}
		}
	}
	
	@Listener
	public void onEntityMove(MoveEntityEvent event){
		Entity entity = event.getTargetEntity();
		if(entity instanceof Player){
			Player player = (Player)entity;
			Optional<DisguisedPlayer> dis = DisguisedPlayer.getDisguise(player);
			if (dis.isPresent()){
				dis.get().updateDisguiseLocation();
			}
		}//else if(DisguisedPlayer.getDisguises().stream().filter(e -> e.getDisguise().isPresent()).anyMatch(e -> e.getDisguise().get().equals(entity))){
	}

}
