package ru.crazylegend.focus.database.migration.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class MigrationConfigurationSection {

    private final String headerId;
    private final List<String> content;
    private final Map<String, String> parameters;

    public MigrationConfigurationSection(@NotNull String headerId) {
        this.headerId = headerId;
        this.content = new ArrayList<>();
        this.parameters = new LinkedHashMap<>();
    }

    public void addContentLine(@NotNull String line) {
        content.add(line);
    }

    public void addParameter(@NotNull String key, @NotNull Object value) {
        parameters.put(key, String.valueOf(value));
    }

    public @NotNull String getHeaderId() {
        return headerId;
    }

    public @NotNull @UnmodifiableView List<String> getContent() {
        return Collections.unmodifiableList(content);
    }

    public @NotNull @UnmodifiableView Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public @NotNull Optional<String> getValue(@NotNull String key) {
        return Optional.ofNullable(parameters.get(key));
    }

    public @NotNull OptionalInt getValueAsInt(@NotNull String key) {
        return getValue(key, Integer::parseInt, OptionalInt::of, OptionalInt::empty);
    }

    public @NotNull OptionalLong getValueAsLong(@NotNull String key) {
        return getValue(key, Long::parseLong, OptionalLong::of, OptionalLong::empty);
    }

    public @NotNull OptionalDouble getValueAsDouble(@NotNull String key) {
        return getValue(key, Double::parseDouble, OptionalDouble::of, OptionalDouble::empty);
    }

    public <V, T> @NotNull T getValue(
            @NotNull String key,
            @NotNull ValueMapper<String, V> valueMapper,
            @NotNull Function<V, T> valueWrapper,
            @NotNull Supplier<T> defaultValue
    ) {
        try {
            String valueRaw = parameters.get(key);
            if (valueRaw != null) {
                V mappedValue = valueMapper.map(valueRaw);
                return valueWrapper.apply(mappedValue);
            }
        } catch (NumberFormatException ignored) {
        }

        return defaultValue.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MigrationConfigurationSection that = (MigrationConfigurationSection) o;
        return Objects.equals(headerId, that.headerId) &&
                Objects.equals(content, that.content) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headerId, content, parameters);
    }

    @Override
    public @NotNull String toString() {
        return "MigrationConfigurationSection{" +
                "headerId='" + headerId + '\'' +
                ", content=" + content +
                ", parameters=" + parameters +
                '}';
    }

    @FunctionalInterface
    public interface ValueMapper<V, T> {

        @Nullable T map(@Nullable V value) throws NumberFormatException;

    }

}
