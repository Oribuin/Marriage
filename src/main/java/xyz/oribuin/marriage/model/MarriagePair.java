package xyz.oribuin.marriage.model;

import java.util.UUID;

public class MarriagePair {

    private final Partner primary;
    private final Partner secondary;
    private long marriedAt;
    private long lastKissed;

    public MarriagePair(UUID primary, UUID secondary) {
        this.primary = new Partner(primary);
        this.secondary = new Partner(secondary);
        this.marriedAt = System.currentTimeMillis();
        this.lastKissed = 0;
    }

    /**
     * Check if a player is married to another player.
     *
     * @param self  The player to check
     * @param other the player to check against
     * @return true if this is the matching pair, false otherwise
     */
    public boolean isMarried(UUID self, UUID other) {
        return this.primary.getUUID().equals(self)
                && this.secondary.getUUID().equals(other)
                || this.primary.getUUID().equals(other)
                && this.secondary.getUUID().equals(self);
    }

    /**
     * Check if a player is inside this marriage pair.
     *
     * @param uuid The uuid to check
     * @return true if the player is inside this pair, false otherwise
     */
    public boolean isEither(UUID uuid) {
        return this.primary.getUUID().equals(uuid) || this.secondary.getUUID().equals(uuid);
    }

    public Partner getPrimary() {
        return primary;
    }

    public Partner getSecondary() {
        return secondary;
    }

    public long getMarriedAt() {
        return this.marriedAt;
    }

    public void setMarriedAt(long marriedAt) {
        this.marriedAt = marriedAt;
    }

    public long getLastKissed() {
        return lastKissed;
    }

    public void setLastKissed(long lastKissed) {
        this.lastKissed = lastKissed;
    }

}
