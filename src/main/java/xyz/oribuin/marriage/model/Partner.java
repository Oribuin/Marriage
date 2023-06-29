package xyz.oribuin.marriage.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class Partner {

    private final @NotNull UUID uuid;
    private @Nullable String name;
    private @Nullable Player cachedPlayer;

    public Partner(@NotNull UUID uuid) {
        this.uuid = uuid;
        this.cachedPlayer = Bukkit.getPlayer(uuid);
        this.name = this.cachedPlayer != null ? this.cachedPlayer.getName() : Bukkit.getOfflinePlayer(uuid).getName();
    }

    public UUID getUUID() {
        return uuid;
    }

    public @Nullable String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public @Nullable Player getCachedPlayer() {
        return cachedPlayer;
    }

    public void setCachedPlayer(@Nullable Player cachedPlayer) {
        this.cachedPlayer = cachedPlayer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (obj.getClass() == this.getClass()) {
            Partner partner = (Partner) obj;
            return Objects.equals(uuid, partner.uuid) && Objects.equals(name, partner.name);
        }

        if (obj.getClass() == UUID.class) {
            return Objects.equals(uuid, obj);
        }

        return false;
    }

}
