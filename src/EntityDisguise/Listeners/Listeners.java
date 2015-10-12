package EntityDisguise.Listeners;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;
import org.spongepowered.api.data.value.mutable.Value;
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
			if(entity.get() != null){
				Optional<ImmutableListValue<Living>> targets = entity.get().getValue(Keys.TARGETS);
				if (targets.isPresent()){
					for (Living living : targets.get().get()){
						if (living instanceof Player){
							Player player = (Player)living;
							Optional<DisguisedPlayer> dis = DisguisedPlayer.getDisguise(player);
							if (dis.isPresent()){
								targets.get().get().remove(player);
							}
						}
					}
					entity.get().offer(Keys.TARGETS, targets.get().get());
					Optional<Value<Location<World>>> target = entity.get().getValue(Keys.TARGETED_LOCATION);
					if (target.isPresent()){
						target.get().get().setPosition(entity.get().getLocation().getPosition());
						entity.get().offer(Keys.TARGETED_LOCATION, target.get().get());
					}
				}
			}
		}
	}
	
	@Listener
	public void onEntityMove(DisplaceEntityEvent.Move.TargetLiving event){
		Living living = event.getTargetEntity();
		System.out.println(living.getType().getName());
		if (living instanceof Player){
			System.out.println("instanceof player");
			Player player = (Player)living;
			Optional<DisguisedPlayer> dis = DisguisedPlayer.getDisguise(player);
			if (dis.isPresent()){
				System.out.println("disguise player found");
				dis.get().updateDisguiseLocation();
				onEntityTarget(dis.get());
			}
		}else{
			for(DisguisedPlayer dis : DisguisedPlayer.getDisguises()){
				if (dis.getDisguise().isPresent()){
					if (dis.getDisguise().get().equals(living)){
						dis.updateDisguiseLocation();
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
