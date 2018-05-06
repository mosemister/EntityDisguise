package org.entitydisguise.entitydisguiseplugin.disguise.data.settings;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;

public class DisguiseSettings implements DataSerializable {

    double deadzone;

    public static final DataQuery DEADZONE_QUERY = DataQuery.of("Deadzone");

    public DisguiseSettings(double deadzone){
        this.deadzone = deadzone;
    }

    public double getDeadzone(){
        return deadzone;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew().set(DEADZONE_QUERY, deadzone);
        return container;
    }
}
