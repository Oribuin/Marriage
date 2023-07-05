package xyz.oribuin.marriage.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import xyz.oribuin.marriage.MarriagePlugin;
import xyz.oribuin.marriage.manager.MarriageManager;
import xyz.oribuin.marriage.model.Couple;

public class PlayerListener implements Listener {

    private final MarriagePlugin plugin;

    public PlayerListener(MarriagePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onKiss(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player target)) return;

        final Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        Couple couple = this.plugin.getManager(MarriageManager.class).getCouple(player.getUniqueId(), target.getUniqueId());
        if (couple == null) return;

        // location between the two players
        couple.kiss(player.getLocation().add(target.getLocation().multiply(0.5)));
    }

}
