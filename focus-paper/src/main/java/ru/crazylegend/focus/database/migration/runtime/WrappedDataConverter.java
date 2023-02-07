package ru.crazylegend.focus.database.migration.runtime;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jsoup.helper.Validate;

import java.util.Objects;

@Getter
public final class WrappedDataConverter<OLD, NEW> {

    private final @NotNull String migrationPath;
    private final boolean usingRelativePath;
    private final @NotNull MigrationDataConverter<OLD, NEW> dataConverter;

    public WrappedDataConverter(@NotNull String migrationPath, boolean usingRelativePath, @NotNull MigrationDataConverter<OLD, NEW> dataConverter) {
        Validate.notEmpty(migrationPath, "migrationPath");
        Validate.notNull(dataConverter, "dataConverter");

        this.migrationPath = migrationPath;
        this.usingRelativePath = usingRelativePath;
        this.dataConverter = dataConverter;
    }

    public @NotNull String getFullMigrationPath(@NotNull String migrationsPathRoot) {
        Validate.notNull(migrationsPathRoot, "migrationsPathRoot");
        String fullMigrationPath = usingRelativePath ? migrationsPathRoot + migrationPath : migrationPath;
        return fullMigrationPath.startsWith("/") ? fullMigrationPath : ("/" + fullMigrationPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrappedDataConverter<?, ?> that = (WrappedDataConverter<?, ?>) o;
        return usingRelativePath == that.usingRelativePath &&
                migrationPath.equals(that.migrationPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(migrationPath, usingRelativePath);
    }

    @Override
    public @NotNull String toString() {
        return "WrappedDataConverter{" +
                "migrationPath='" + migrationPath + '\'' +
                ", usingRelativePath=" + usingRelativePath +
                ", dataConverter=" + dataConverter +
                '}';
    }

}
