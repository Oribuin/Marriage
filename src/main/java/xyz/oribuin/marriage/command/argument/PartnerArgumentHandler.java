package xyz.oribuin.marriage.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import xyz.oribuin.marriage.manager.DataManager;
import xyz.oribuin.marriage.model.Partner;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PartnerArgumentHandler extends RoseCommandArgumentHandler<Partner> {

    public PartnerArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, Partner.class);
    }

    @Override
    protected Partner handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) throws HandledArgumentException {
        String input = argumentParser.next();
        if (!(argumentParser.getContext() instanceof Player player))
            throw new HandledArgumentException("only-player");

        Partner partner = this.rosePlugin.getManager(DataManager.class)
                .getPartners(player.getUniqueId())
                .stream()
                .filter(p -> p.getName() != null && p.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);

        if (partner == null)
            throw new HandledArgumentException("invalid-partner", StringPlaceholders.of("input", input));

        return partner;
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();

        if (!(argumentParser.getContext() instanceof Player player))
            return List.of();
        return this.rosePlugin.getManager(DataManager.class)
                .getPartners(player.getUniqueId())
                .stream()
                .map(Partner::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
