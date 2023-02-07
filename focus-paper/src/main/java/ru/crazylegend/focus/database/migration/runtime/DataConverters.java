package ru.crazylegend.focus.database.migration.runtime;

import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.database.migration.annotation.MigrationPath;
import ru.crazylegend.focus.database.migration.exception.PathAnnotationNotFoundException;

public final class DataConverters {

    @SuppressWarnings("unchecked")
    public static <OLD, NEW> @NotNull WrappedDataConverter<OLD, NEW> wrap(@NotNull MigrationDataConverter<OLD, NEW> dataConverter) throws PathAnnotationNotFoundException {
        Class<?> dataConverterClass = dataConverter.getClass();
        MigrationPath typeAnnotation = dataConverterClass.getAnnotation(MigrationPath.class);
        if (typeAnnotation == null)
            throw new PathAnnotationNotFoundException((Class<? extends MigrationDataConverter<?, ?>>) dataConverterClass);

        String migrationPath = typeAnnotation.value();
        boolean usingRelativePath = typeAnnotation.useRelativePath();

        return new WrappedDataConverter<>(migrationPath, usingRelativePath, dataConverter);
    }

    public static <OLD, NEW> @NotNull WrappedDataConverter<OLD, NEW> wrap(
            @NotNull String migrationPath,
            boolean useRelativePath,
            @NotNull MigrationDataConverter<OLD, NEW> dataConverter
    ) {
        return new WrappedDataConverter<>(migrationPath, useRelativePath, dataConverter);
    }

}
