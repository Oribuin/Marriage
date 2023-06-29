package xyz.oribuin.marriage.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class VaultProvider {

    private static VaultProvider instance;
    private @Nullable Economy economy;

    public VaultProvider() {
        instance = this;

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            this.economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
        }

    }

    public static VaultProvider getInstance() {
        return instance;
    }

}
