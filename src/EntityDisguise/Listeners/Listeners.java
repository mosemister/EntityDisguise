package EntityDisguise.Listeners;

import java.util.List;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import EntityDisguise.Data.DisguisedPlayer;

import com.google.common.base.Optional;

public class Listeners {
	
	public void onEntityTarget(DisguisedPlayer disguised){
		Optional<Entity> entity = disguised.getDisguise();
		if (entity.isPresent()){
			Optional<List<Living>> targets = entity.get().get(Keys.TARGETS);
			if (targets.isPresent()){
				for (Living living : targets.get()){
					if (living instanceof Player){
						Player player = (Player)living;
						Optional<DisguisedPlayer> dis = DisguisedPlayer.getDisguise(player);
						if (dis.isPresent()){
							targets.get().remove(player);
						}
					}
				}
				entity.get().offer(Keys.TARGETS, targets.get());
				Optional<Location<World>> target = entity.get().get(Keys.TARGETED_LOCATION);
				if (target.isPresent()){
					target.get().setPosition(entity.get().getLocation().getPosition());
					entity.get().offer(Keys.TARGETED_LOCATION, target.get());
				}
			}
		}
	}
	
	@Listener
	public void onEntityMove(DisplaceEntityEvent.Move.TargetLiving event){
		Living living = event.getTargetEntity();
		if (living instanceof Player){
			Player player = (Player)living;
			Optional<DisguisedPlayer> dis = DisguisedPlayer.getDisguise(player);
			if (dis.isPresent()){
				dis.get().updateDisguise();
				onEntityTarget(dis.get());
			}
		}else{
			for(DisguisedPlayer dis : DisguisedPlayer.getDisguises()){
				if (dis.getDisguise().isPresent()){
					if (dis.getDisguise().get().equals(living)){
						dis.updateDisguise();
						onEntityTarget(dis);
					}
				}
			}
		}
	}
	
	@Listener
	public void onEntityDamage(DamageEntityEvent event){
		Entity entity = event.getTargetEntity();
		for(DisguisedPlayer player : DisguisedPlayer.getDisguises()){
			if (player.getDisguise().isPresent()){
				if (player.getDisguise().get().equals(entity)){
					HealthData data = player.getPlayer().getHealthData();
					player.getPlayer().offer(Keys.HEALTH, data.health().get() - event.getBaseDamage());
					event.setBaseDamage(0);
				}
			}
		}
		
	}

}
