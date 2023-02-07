package ru.crazylegend.focus.database.migration.schema;

import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.util.tool.Validate;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

@FunctionalInterface
public interface DatabaseSchemaAnalyzer {

    static @NotNull DatabaseSchemaAnalyzer checkTableStructures(@NotNull Class<?>... tableClasses) {
        Validate.notEmpty(tableClasses, "tableClasses");
        return new TableStructuresAnalyzer(new LinkedHashSet<>(Arrays.asList(tableClasses)));
    }

    static @NotNull DatabaseSchemaAnalyzer checkTableStructures(@NotNull Collection<Class<?>> tableClasses) {
        Validate.notEmpty(tableClasses, "tableClasses");
        return new TableStructuresAnalyzer(new LinkedHashSet<>(tableClasses));
    }

    boolean analyze(@NotNull ConnectionSource connectionSource, int validatingVersion) throws SQLException;

}
