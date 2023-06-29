package xyz.oribuin.marriage.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.marriage.manager.ConfigurationManager.Setting;
import xyz.oribuin.marriage.model.MarriagePair;
import xyz.oribuin.marriage.model.Partner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MarriageManager extends Manager {

    // Sender, Receiver
    private final Cache<UUID, UUID> requests = CacheBuilder.newBuilder()
            .expireAfterWrite(Setting.REQUEST_TIMEOUT.getInt(), TimeUnit.SECONDS)
            .build();

    private final List<MarriagePair> requests = new ArrayList<>();

    public MarriageManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {

    }

    /**
     * Get a marriage pair from a player
     *
     * @param pair The pair to check
     */
    public void send(@NotNull MarriagePair pair) {
        this.requests.put(pair.getPrimary().getUUID(), pair.getSecondary().getUUID());
    }

    /**
     * Remove a marriage pair from a player
     *
     * @param pair The pair to remove
     */
    public void deny(@NotNull MarriagePair pair) {
        this.requests.invalidate(pair.getPrimary().getUUID());
    }

    /**
     * Check if a player is married
     *
     * @param uuid The uuid to check
     * @return If the player is married
     */
    public boolean isMarried(@NotNull UUID uuid) {
        return this.rosePlugin.getManager(DataManager.class).getPartners(uuid).size() > 0;
    }

    /**
     * Accept a marriage pair from a player
     *
     * @param pair The pair to accept
     */
    public void marry(@NotNull MarriagePair pair) {
        this.rosePlugin.getManager(DataManager.class).create(pair);
        this.requests.invalidate(pair.getPrimary().getUUID());
    }

    /**
     * Divorce a marriage pair
     *
     * @param pair The pair to divorce
     */
    public void divorce(@NotNull MarriagePair pair) {
        this.rosePlugin.getManager(DataManager.class).delete(pair);
    }

    /**
     * Get all the marriage requests a player has
     *
     * @param target The uuid to check
     * @return The marriage pair
     */
    @NotNull
    public List<MarriagePair> getRequests(@NotNull UUID target) {
        return this.requests.asMap().values().stream()
                .filter(uuid -> uuid.equals(target))
                .map(uuid -> new MarriagePair(uuid, target))
                .collect(Collectors.toList());
    }


    /**
     * Get the total partners a player has
     *
     * @param uuid The uuid to check
     * @return The total partners
     */
    @NotNull
    public List<Partner> getPartners(@NotNull UUID uuid) {
        return this.rosePlugin.getManager(DataManager.class).getPartners(uuid);
    }

    /**
     * Get all the marriages a player has
     *
     * @param uuid The uuid to check
     * @return The marriages
     */
    @NotNull
    public List<MarriagePair> getMarriages(@NotNull UUID uuid) {
        return this.rosePlugin.getManager(DataManager.class).getCachedPairs()
                .stream()
                .filter(pair -> pair.isEither(uuid))
                .collect(Collectors.toList());
    }


}
