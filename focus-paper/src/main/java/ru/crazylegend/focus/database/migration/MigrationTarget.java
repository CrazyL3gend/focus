package ru.crazylegend.focus.database.migration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.database.DatabaseType;
import ru.crazylegend.focus.util.tool.Validate;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MigrationTarget {

    ALL(null),
    SQLITE(DatabaseType.SQLITE),
    MYSQL(DatabaseType.MYSQL),
    POSTGRESQL(DatabaseType.POSTGRESQL);

    private final DatabaseType databaseType;

    public static @NotNull MigrationTarget getByKey(@Nullable String key) {
        if (key == null || key.isEmpty())
            return ALL;

        for (MigrationTarget target : values())
            if (target.name().equalsIgnoreCase(key))
                return target;

        return ALL;
    }

    public static @NotNull Set<MigrationTarget> getByKeys(@Nullable String targets) {
        if (targets == null || targets.isEmpty())
            return Collections.emptySet();

        String[] keys = targets.split(",\\s?");
        Set<MigrationTarget> output = new LinkedHashSet<>();

        for (String key : keys)
            output.add(getByKey(key));

        return output;
    }

    public @NotNull DatabaseType getDatabaseType() {
        if (!hasCertainDatabaseType())
            throw new IllegalStateException(String.format("Migration target '%s' hasn't certain database type!", name().toLowerCase()));

        return databaseType;
    }

    public boolean hasCertainDatabaseType() {
        return databaseType != null;
    }

    public boolean isApplicableFor(@NotNull DatabaseType databaseType) {
        Validate.notNull(databaseType, "databaseType");
        return !hasCertainDatabaseType() || this.databaseType == databaseType;
    }

}
