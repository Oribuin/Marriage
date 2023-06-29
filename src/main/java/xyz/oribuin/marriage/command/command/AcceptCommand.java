package xyz.oribuin.marriage.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import xyz.oribuin.marriage.manager.LocaleManager;
import xyz.oribuin.marriage.manager.MarriageManager;
import xyz.oribuin.marriage.model.MarriagePair;

import java.util.List;

public class AcceptCommand extends RoseCommand {

    public AcceptCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, @Optional Player from) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);
        final MarriageManager manager = this.rosePlugin.getManager(MarriageManager.class);
        final Player player = (Player) context.getSender();

        List<MarriagePair> pairs = manager.getRequests(player.getUniqueId()); // Get the requests sent to the player.

        if (from != null && pairs.size() > 1) {
            pairs.removeIf(pair -> pair.isEither(player.getUniqueId()) && !pair.isEither(from.getUniqueId()));
        }

        MarriagePair pair = pairs.stream().findFirst().orElse(null);
        if (pair == null) {
            locale.sendMessage(player, "command-accept-no-request", StringPlaceholders.of("player", from == null ? "none" : from.getName()));
            return;
        }


        manager.marry(pair);
        StringPlaceholders placeholders = StringPlaceholders.of("player", player.getName(), "target", from.getName());
        locale.sendMessage(player, "command-accept-accepted", placeholders);
        locale.sendMessage(from, "command-accept-accepted", placeholders);
    }

    @Override
    protected String getDefaultName() {
        return "accept";
    }

    @Override
    public String getDescriptionKey() {
        return "command-accept-description";
    }

    @Override
    public String getRequiredPermission() {
        return "marriage.command.accept";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }
}

