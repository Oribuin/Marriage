package xyz.oribuin.marriage.database.migration;

import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class _1_CreateInitialTables extends DataMigration {

    public _1_CreateInitialTables() {
        super(1);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        final String marriageTable = "CREATE TABLE IF NOT EXISTS `" + tablePrefix + "` (" +
                "`primary` VARCHAR(36) NOT NULL," +
                "`secondary` VARCHAR(36) NOT NULL," +
                "`married_at` LONG NOT NULL DEFAULT 0)";

        try (PreparedStatement statement = connection.prepareStatement(marriageTable)) {
            statement.executeUpdate();
        }
    }

}
