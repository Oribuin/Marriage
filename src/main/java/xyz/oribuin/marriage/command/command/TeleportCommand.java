package xyz.oribuin.marriage.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import xyz.oribuin.marriage.manager.LocaleManager;
import xyz.oribuin.marriage.model.Partner;

public class TeleportCommand extends RoseCommand {

    public TeleportCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Partner partner) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);
        final Player player = (Player) context.getSender();
        final Player target = partner.getCachedPlayer();

        if (target == null || !target.isOnline()) {
            locale.sendMessage(player, "command-teleport-not-online");
            return;
        }

        if (NMSUtil.isPaper())
            player.teleportAsync(target.getLocation());
        else
            player.teleport(target);

        locale.sendMessage(player, "command-teleport-success", StringPlaceholders.of("target", target.getName()));
    }

    @Override
    protected String getDefaultName() {
        return "teleport";
    }

    @Override
    public String getDescriptionKey() {
        return "command-teleport-description";
    }

    @Override
    public String getRequiredPermission() {
        return "marriage.command.teleport";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

}
