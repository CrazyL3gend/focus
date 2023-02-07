package ru.crazylegend.focus.database.migration.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.database.migration.runtime.MigrationDataConverter;

@Getter
public final class PathAnnotationNotFoundException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "@MigrationPath annotation must be specified for the %s data converter!";

    private final Class<? extends MigrationDataConverter<?, ?>> dataConverterType;

    public PathAnnotationNotFoundException(@NotNull Class<? extends MigrationDataConverter<?, ?>> dataConverterType) {
        super(String.format(MESSAGE_FORMAT, dataConverterType.getName()));
        this.dataConverterType = dataConverterType;
    }

}
