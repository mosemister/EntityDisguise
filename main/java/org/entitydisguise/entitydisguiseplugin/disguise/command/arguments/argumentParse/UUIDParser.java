package org.entitydisguise.entitydisguiseplugin.disguise.command.arguments.argumentParse;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UUIDParser extends AbstractParser<UUID> {

    @Override
    public Class<UUID> getParseType() {
        return UUID.class;
    }

    @Override
    public UUID parse(String value) throws CommandException {
        try {
            return UUID.fromString(value);
        }catch(IllegalArgumentException e){
        }
        try {
            Optional<ProviderRegistration<UserStorageService>> opStorage = Sponge.getServiceManager().getRegistration(UserStorageService.class);
            if (!opStorage.isPresent()) {
                throw new CommandException(Text.of("Can not gain user storage"));
            }
            UserStorageService service = opStorage.get().getProvider();
            Optional<User> opUser = service.get(value);
            if (!opUser.isPresent()) {
                throw new CommandException(Text.of("Unknown player"));
            }
            return opUser.get().getUniqueId();
        }catch(IllegalArgumentException e){
            throw new CommandException(Text.of("Unknown player"));
        }
    }

    @Override
    public List<String> getSuggested(String value) {
        Optional<ProviderRegistration<UserStorageService>> opStorage = Sponge.getServiceManager().getRegistration(UserStorageService.class);
        if(!opStorage.isPresent()){
            return new ArrayList<>();
        }
        UserStorageService service = opStorage.get().getProvider();
        List<String> users = new ArrayList<>();
        service.getAll().stream().filter(g -> g.getName().isPresent()).filter(g -> g.getName().get().toLowerCase().contains(value.toLowerCase())).forEach(g -> users.add(g.getName().get()));
        return users;
    }
}
