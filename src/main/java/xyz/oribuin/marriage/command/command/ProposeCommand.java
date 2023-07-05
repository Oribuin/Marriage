package xyz.oribuin.marriage.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import xyz.oribuin.marriage.hook.VaultProvider;
import xyz.oribuin.marriage.manager.ConfigurationManager.Setting;
import xyz.oribuin.marriage.manager.LocaleManager;
import xyz.oribuin.marriage.manager.MarriageManager;
import xyz.oribuin.marriage.model.Couple;

import java.util.List;

public class ProposeCommand extends RoseCommand {

    public ProposeCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }


    @RoseExecutable
    public void execute(CommandContext context, Player target) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);
        final MarriageManager manager = this.rosePlugin.getManager(MarriageManager.class);
        final Player player = (Player) context.getSender();

        List<Couple> selfPairs = manager.getMarriages(player.getUniqueId());
        List<Couple> targetPairs = manager.getMarriages(target.getUniqueId());

        if (selfPairs.stream().anyMatch(pair -> pair.isMarried(player.getUniqueId(), target.getUniqueId()))) {
            locale.sendMessage(player, "command-propose-already-married", StringPlaceholders.of("target", target.getName()));
            return;
        }

        if (selfPairs.size() >= Setting.MAX_PARTNERS.getInt() || targetPairs.size() >= Setting.MAX_PARTNERS.getInt()) {
            locale.sendMessage(player, "command-propose-max-partners", StringPlaceholders.of("target", target.getName()));
            return;
        }

        int marriageCost = Setting.MARRIAGE_COST.getInt();
        if (marriageCost > 0 && VaultProvider.isEnabled()) {

            if (!VaultProvider.getInstance().has(player, marriageCost)) {
                locale.sendMessage(player, "command-propose-no-money");
                return;
            }

            VaultProvider.getInstance().take(player, marriageCost);
        }

        manager.propose(new Couple(player.getUniqueId(), target.getUniqueId()));
        locale.sendMessage(target, "command-propose-received",
                StringPlaceholders.of("player", player.getName(),
                        "target", target.getName()
                ));
        locale.sendMessage(player, "command-propose-sent",
                StringPlaceholders.of("target", target.getName(),
                        "player", player.getName()
                )
        );
    }

    @Override
    protected String getDefaultName() {
        return "propose";
    }

    @Override
    public String getDescriptionKey() {
        return "command-propose-description";
    }

    @Override
    public String getRequiredPermission() {
        return "marriage.command.propose";
    }

}
