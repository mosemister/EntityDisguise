package org.entitydisguise.entitydisguiseplugin.disguise.data.disguise;

import org.entitydisguise.entitydisguiseplugin.disguise.data.EntityDisguiseKeys;
import org.entitydisguise.entitydisguiseplugin.disguise.data.settings.DisguiseSettings;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class DisguiseEntity implements DataSerializable {

    public static final DataQuery ENTITY_SNAPSHOT = DataQuery.of("ENTITY_SNAPSHOT");

    private EntitySnapshot snapshot;
    private Entity entity;

    public DisguiseEntity(){

    }

    public DisguiseEntity(Entity entity){
        this(entity.createSnapshot());
        this.entity = entity;
    }

    public DisguiseEntity(EntitySnapshot snapshot){
        this.snapshot = snapshot;
    }

    public void setSnapshot(EntitySnapshot snapshot){
        destroy();
        this.snapshot = snapshot;
    }

    public void setEntity(Entity entity){
        if(snapshot == null){
            throw new NullPointerException("Snapshot requires to be set before DisguiseEntity.setEntity(Entity) can be used");
        }
        if(!snapshot.getType().getId().equals(entity.getType().getId())){
            throw new IllegalArgumentException("EntityType needs to be the same as snapshot in DisguiseEntity.setEntity(Entity)");
        }
        destroy();
        this.entity = entity;
    }

    public Optional<EntitySnapshot> getSnapshot(){
        return Optional.ofNullable(snapshot);
    }

    public Optional<Entity> getEntity(){
        return Optional.ofNullable(entity);
    }

    public void destroy(){
        if(entity != null){
            entity.remove();
        }
    }

    public void setVisible(boolean check){
        offer(Keys.VANISH, !check);
        offer(Keys.VANISH_IGNORES_COLLISION, !check);
        offer(Keys.VANISH_PREVENTS_TARGETING, !check);
    }

    public Optional<Entity> safeUpdate(Entity linked){
        if(snapshot == null){
            return Optional.empty();
        }
        Entity entity;
            if(getEntity().isPresent()){
                entity = this.entity;
            }else{
                return Optional.empty();
            }
        Optional<DisguiseSettings> opSettings = linked.get(EntityDisguiseKeys.KEY_DISGUISE_SETTINGS);
        if(!opSettings.isPresent()){
            return Optional.empty();
        }
        double distance = linked.getLocation().getPosition().distance(entity.getLocation().getPosition());
        if(distance > opSettings.get().getDeadzone()){
            return forceUpdate(linked.getTransform());
        }
        return Optional.empty();
    }

    public Optional<Entity> forceUpdate(Transform<World> transform){
        if(snapshot == null){
            return Optional.empty();
        }
        Entity entity;
            if(getEntity().isPresent()){
                entity = this.entity;
            }else{
                return Optional.empty();
            }
        entity.setTransform(transform);
        return Optional.of(entity);
    }

    public boolean supports(Key<BaseValue<? extends Object>> key){
        if (entity != null) {
            return entity.supports(key);
        }
        if(snapshot != null) {
            return snapshot.supports(key);
        }
        return false;
    }

    public <E extends Object> boolean offer(Key<? extends BaseValue<E>> key, E value){
        if(snapshot == null){
            return false;
        }
        Optional<EntitySnapshot> with = snapshot.with(key, value);
        boolean check = false;
        if(with.isPresent()){
            snapshot = snapshot.with(key, value).get();
            check = true;
        }
        if(entity != null) {
            entity.offer(key, value);
            return true;
        }
        return check;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew();
        /*if(snapshot != null){
            return container.set(ENTITY_SNAPSHOT, snapshot);
        }*/
        return container;
    }
}
