package EntityDisguise.Data;

import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;

public enum CreatureTypes {
	
	ZOMBIE(EntityTypes.ZOMBIE),
	SPIDER(EntityTypes.SPIDER),
	CAVE_SPIDER(EntityTypes.CAVE_SPIDER),
	ENDERMAN(EntityTypes.ENDERMAN),
	SHEEP(EntityTypes.SHEEP),
	SILVERFISH(EntityTypes.SILVERFISH),
	SKELETON(EntityTypes.SKELETON),
	BLAZE(EntityTypes.BLAZE),
	CHICKEN(EntityTypes.CHICKEN),
	GIANT(EntityTypes.GIANT),
	WOLF(EntityTypes.WOLF),
	HORSE(EntityTypes.HORSE),
	VILLAGER(EntityTypes.VILLAGER);
	
	EntityType TYPE;
	
	CreatureTypes(EntityType type){
		TYPE = type;
	}
	
	public EntityType getType(){
		return TYPE;
	}
	
	public static EntityType getType(String name){
		for(CreatureTypes type : CreatureTypes.values()){
			if (type.name().equalsIgnoreCase(name)){
				return type.TYPE;
			}
		}
		return null;
	}
}
