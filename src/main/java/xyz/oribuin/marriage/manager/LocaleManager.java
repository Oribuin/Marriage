package xyz.oribuin.marriage.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.StringPlaceholders;

public class LocaleManager extends AbstractLocaleManager {

    public LocaleManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    /**
     * Send a message to all online players
     *
     * @param key          The message key
     * @param placeholders The placeholders
     */
    public void sendAll(String key, StringPlaceholders placeholders) {
        String message = this.getLocaleMessage(key, placeholders);
        this.rosePlugin.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

}
