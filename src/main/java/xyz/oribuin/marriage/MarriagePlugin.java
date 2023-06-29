package xyz.oribuin.marriage;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import xyz.oribuin.marriage.manager.CommandManager;
import xyz.oribuin.marriage.manager.ConfigurationManager;
import xyz.oribuin.marriage.manager.DataManager;
import xyz.oribuin.marriage.manager.LocaleManager;
import xyz.oribuin.marriage.manager.MarriageManager;

import java.util.List;

public class MarriagePlugin extends RosePlugin {

    private static MarriagePlugin instance;

    public static MarriagePlugin getInstance() {
        return instance;
    }

    public MarriagePlugin() {
        super(-1, -1,
                ConfigurationManager.class,
                DataManager.class,
                LocaleManager.class,
                CommandManager.class
        );

        instance = this;
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(MarriageManager.class);
    }

}
