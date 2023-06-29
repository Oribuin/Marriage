package xyz.oribuin.marriage.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.command.BaseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import xyz.oribuin.marriage.manager.ConfigurationManager.Setting;
import xyz.oribuin.marriage.manager.DataManager;
import xyz.oribuin.marriage.manager.LocaleManager;
import xyz.oribuin.marriage.manager.MarriageManager;
import xyz.oribuin.marriage.model.MarriagePair;
import xyz.oribuin.marriage.model.Partner;

import java.util.List;

public class MarryCommand extends BaseCommand {

    public MarryCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Player target) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);
        final MarriageManager manager = this.rosePlugin.getManager(MarriageManager.class);
        final Player player = (Player) context.getSender();

        List<MarriagePair> selfPairs = manager.getMarriages(player.getUniqueId());
        List<MarriagePair> targetPairs = manager.getMarriages(target.getUniqueId());

        if (selfPairs.stream().anyMatch(pair -> pair.isMarried(player.getUniqueId(), target.getUniqueId()))) {
            locale.sendMessage(player, "command-marry-already-married");
            return;
        }

        if (selfPairs.size() >= Setting.MAX_PARTNERS.getInt() || targetPairs.size() >= Setting.MAX_PARTNERS.getInt()) {
            locale.sendMessage(player, "command-marry-max-partners");
            return;
        }

        manager.send(new MarriagePair(player.getUniqueId(), target.getUniqueId()));
        locale.sendMessage(player, "command-marry-send-sent",
                StringPlaceholders.of("target", target.getName(),
                        "player", player.getName()
                )
        );
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

}
