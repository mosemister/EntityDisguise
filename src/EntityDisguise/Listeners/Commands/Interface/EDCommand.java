package EntityDisguise.Listeners.Commands.Interface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public abstract class EDCommand implements CommandCallable{

	static List<EDCommand> COMMANDS = new ArrayList<EDCommand>();

	public abstract boolean runPlayerCommand(Player player, String[] args);
	public abstract boolean hasPermission(Player player);
	public abstract boolean runConsoleCommand(ConsoleSource sender, String[] args);
	public abstract String getCommandName();
	public abstract String getUsage();
	
	public EDCommand(){
		COMMANDS.add(this);
	}
	
	@Override
	public Optional<? extends Text> getHelp(CommandSource arg0) {
		return Optional.of(Text.of("All EntityDisguised commands"));
	}

	@Override
	public Optional<? extends Text> getShortDescription(CommandSource arg0) {
		return Optional.of(Text.of("All EntityDisguised commands"));
	}

	@Override
	public List<String> getSuggestions(CommandSource arg0, String arg1) throws CommandException {
		return Collections.emptyList();
	}

	@Override
	public Text getUsage(CommandSource arg0) {
		return Text.of(getUsage());
	}

	@Override
	public CommandResult process(CommandSource arg0, String arg1) throws CommandException {
		String[] args = arg1.split(" ");
		System.out.println("running entity disguise command");
		if (arg0 instanceof Player){
			System.out.println("commandsource is player");
			if (hasPermission((Player)arg0)){
				System.out.println("has permission");
				if (runPlayerCommand((Player)arg0, args)){
					System.out.println("success");
					CommandResult.success();
				}
			}
		}else if (arg0 instanceof ConsoleSource){
			if(runConsoleCommand((ConsoleSource)arg0, args)){
				CommandResult.success();
			}
		}
		return CommandResult.empty();
	}

	@Override
	public boolean testPermission(CommandSource arg0) {
		return true;
	}

}
