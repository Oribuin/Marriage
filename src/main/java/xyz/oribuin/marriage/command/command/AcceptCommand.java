package xyz.oribuin.marriage.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import xyz.oribuin.marriage.manager.ConfigurationManager.Setting;
import xyz.oribuin.marriage.manager.LocaleManager;
import xyz.oribuin.marriage.manager.MarriageManager;
import xyz.oribuin.marriage.model.Couple;

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

        List<Couple> pairs = manager.getRequests(player.getUniqueId()); // Get the requests sent to the player.

        if (from == null && pairs.size() == 1) {
            from = pairs.get(0).getSecondary().getCachedPlayer();
        }

        if (from != null && pairs.size() > 1) {
            Player finalFrom = from;
            pairs.removeIf(pair -> pair.isEither(player.getUniqueId()) && !pair.isEither(finalFrom.getUniqueId()));
        }



        StringPlaceholders placeholders = StringPlaceholders.of("player", player.getName(), "target", from == null ? "none" : from.getName());

        Couple pair = pairs.stream().findFirst().orElse(null);
        if (pair == null) {
            locale.sendMessage(player, "command-accept-no-request", placeholders);
            return;
        }

        if (from == null && pair.getSecondary().getCachedPlayer() == null) {
            locale.sendMessage(player, "command-accept-no-request", placeholders);
            return;
        }

        manager.removeRequest(pair.getPrimary().getUUID()); // Remove the request.
        manager.marry(pair);

        locale.sendMessage(player, "command-accept-success", StringPlaceholders.of("other", from == null ? "none" : from.getName()));
        locale.sendMessage(from, "command-accept-success", StringPlaceholders.of("other", player.getName()));

        if (Setting.MARRIAGE_ANNOUNCEMENT.getBoolean())
            locale.sendAll("command-accept-announce", placeholders);

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

