package ru.crazylegend.focus.database.migration.runtime;

import com.j256.ormlite.support.DatabaseConnection;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.database.migration.Migration;
import ru.crazylegend.focus.database.migration.exception.MigrationRunException;
import ru.crazylegend.focus.util.tool.Validate;

import java.sql.SQLException;
import java.util.List;

public class MigrationRunner {

    private final Migration migration;
    private final DatabaseConnection migrationConnection;

    public MigrationRunner(@NotNull Migration migration, @NotNull DatabaseConnection migrationConnection) {
        Validate.notNull(migration, "migration");
        Validate.notNull(migrationConnection, "migrationConnection");

        this.migration = migration;
        this.migrationConnection = migrationConnection;
    }

    public boolean runStatements() throws MigrationRunException {
        if (!migration.hasSQLStatements())
            return false;

        try {
            List<String> statements = migration.getSQLStatements();
            for (String statement : statements) {
                migrationConnection.executeStatement(statement, DatabaseConnection.DEFAULT_RESULT_FLAGS);
            }
            return true;
        } catch (SQLException ex) {
            throw new MigrationRunException(migration, ex);
        }
    }

}
