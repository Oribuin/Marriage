package xyz.oribuin.marriage.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.manager.AbstractDataManager;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.marriage.database.migration._1_CreateInitialTables;
import xyz.oribuin.marriage.model.Couple;
import xyz.oribuin.marriage.model.Partner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class DataManager extends AbstractDataManager {

    private final Set<Couple> cachedPairs = new HashSet<>();

    public DataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    public void loadPairs() {
        this.cachedPairs.clear();

        this.async(() -> this.databaseConnector.connect(connection -> {
            final String query = "SELECT * FROM `" + this.getTablePrefix() + "couples`";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        final UUID primary = UUID.fromString(resultSet.getString("primary"));
                        final UUID secondary = UUID.fromString(resultSet.getString("secondary"));

                        final Couple pair = new Couple(
                                primary,
                                secondary
                        );

                        pair.setMarriedAt(resultSet.getLong("married_at"));

                        this.cachedPairs.add(pair);
                    }
                }
            }
        }));
    }

    /**
     * Create a new marriage pair in the database
     *
     * @param pair The pair to create
     */
    public void create(@NotNull Couple pair) {
        this.cachedPairs.add(pair);

        this.async(() -> this.databaseConnector.connect(connection -> {
            final String sql = "INSERT INTO `" + this.getTablePrefix() + "couples` (`primary`, `secondary`, `married_at`) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, pair.getPrimary().getUUID().toString());
                statement.setString(2, pair.getSecondary().getUUID().toString());
                statement.setLong(3, pair.getMarriedAt());
                statement.executeUpdate();
            }
        }));
    }

    /**
     * Delete a marriage pair from the database
     *
     * @param pair The pair to delete
     */
    public void delete(@NotNull Couple pair) {
        this.cachedPairs.remove(pair);

        this.async(() -> this.databaseConnector.connect(connection -> {
            final String sql = "DELETE FROM `" + this.getTablePrefix() + "couples` WHERE `primary` = ? AND `secondary` = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, pair.getPrimary().getUUID().toString());
                statement.setString(2, pair.getPrimary().getUUID().toString());
                statement.executeUpdate();
            }
        }));
    }

    /**
     * Check if a player is married
     *
     * @param self The player to check
     * @return If the player is married
     */
    public boolean isPartner(UUID self, UUID target) {
        return this.cachedPairs.stream().anyMatch(pair -> pair.isMarried(self, target));
    }

    /**
     * Get all partners of a player
     *
     * @param self The player to get partners of
     * @return The partners of the player
     */
    public List<Partner> getPartners(UUID self) {
        return this.cachedPairs.stream()
                .map(pair -> {
                    if (pair.getPrimary().getUUID().equals(self))
                        return pair.getSecondary();

                    if (pair.getSecondary().getUUID().equals(self))
                        return pair.getPrimary();
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Get all cached marriage pairs
     *
     * @return The cached marriage pairs
     */
    public Set<Couple> getCachedPairs() {
        return this.cachedPairs;
    }

    @Override
    public List<Class<? extends DataMigration>> getDataMigrations() {
        return List.of(_1_CreateInitialTables.class);
    }

    /**
     * Run a task asynchronously
     *
     * @param runnable The task to run
     */
    private void async(Runnable runnable) {
        this.rosePlugin.getServer().getScheduler().runTaskAsynchronously(this.rosePlugin, runnable);
    }

}
