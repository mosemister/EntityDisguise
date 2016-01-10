package EntityDisguise.Listeners;

import java.util.Optional;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;

import EntityDisguise.Data.DisguisedPlayer;

public class Listeners {
	
	@Listener
	public void onEntityDamage(DamageEntityEvent event){
		Entity entity = event.getTargetEntity();
		for(DisguisedPlayer player : DisguisedPlayer.getDisguises()){
			if (player.getDisguise().isPresent()){
				if (!event.getCause().equals(player.getPlayer())){
					if (player.getDisguise().get().equals(entity)){
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
	public void onEntityMove(DisplaceEntityEvent.Move.TargetPlayer event){
		Player player = event.getTargetEntity();
		Optional<DisguisedPlayer> dis = DisguisedPlayer.getDisguise(player);
		if (dis.isPresent()){
			dis.get().updateDisguiseLocation();
		}
	}

}
