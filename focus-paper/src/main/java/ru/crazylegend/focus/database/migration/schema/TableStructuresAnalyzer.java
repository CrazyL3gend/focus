package ru.crazylegend.focus.database.migration.schema;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Set;

@Getter
public final class TableStructuresAnalyzer implements DatabaseSchemaAnalyzer {

    private final Set<Class<?>> tableClasses;

    public TableStructuresAnalyzer(@NotNull Set<Class<?>> tableClasses) {
        this.tableClasses = tableClasses;
    }

    @Override
    public boolean analyze(@NotNull ConnectionSource connectionSource, int validatingVersion) throws SQLException {
        if (tableClasses.isEmpty())
            return true;

        for (Class<?> tableClass : tableClasses)
            DaoManager.createDao(connectionSource, tableClass).queryBuilder().queryForFirst();

        return true;
    }

}
