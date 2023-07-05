package xyz.oribuin.marriage.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;

import java.util.List;

public class MarriageCommandWrapper extends RoseCommandWrapper {

    public MarriageCommandWrapper(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public String getDefaultName() {
        return "marriage";
    }

    @Override
    public List<String> getDefaultAliases() {
        return List.of("marry");
    }

    @Override
    public List<String> getCommandPackages() {
        return List.of("xyz.oribuin.marriage.command.command");
    }

    @Override
    public boolean includeBaseCommand() {
        return true;
    }

    @Override
    public boolean includeHelpCommand() {
        return true;
    }

    @Override
    public boolean includeReloadCommand() {
        return true;
    }

}
