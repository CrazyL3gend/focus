package ru.crazylegend.focus.util.function;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Optionality<T> {

    private T value;

    private Optionality(T value) {
        this.value = value;
    }

    public static <T> Optionality<T> empty() {
        return new Optionality<>(null);
    }

    public static <T> Optionality<T> optionalOf(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Null value");
        }
        return new Optionality<>(value);
    }

    public static <T> Optionality<T> optionalOfNullable(T value) {
        return new Optionality<>(value);
    }

    public static <T> Optionality<T> optionalOf(Supplier<T> value) {
        return new Optionality<>(value.get());
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> Optionality<T> convert(Optional<T> optional) {
        return new Optionality<>(optional.orElse(null));
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("Value absent!");
        }
        return value;
    }

    public T orElse(T value) {
        return isPresent() ? get() : value;
    }

    public T orElseGet(Supplier<T> value) {
        return isPresent() ? get() : value.get();
    }

    public T orElseThrow(Supplier<Throwable> value) throws Throwable {
        if (isPresent()) {
            throw value.get();
        }
        return get();
    }

    public Optionality<T> ifPresent(Consumer<T> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
        return this;
    }

    public Optionality<T> ifAbsent(Runnable runnable) {
        if (!isPresent()) {
            runnable.run();
        }
        return this;
    }

    public Optionality<T> filter(Predicate<T> predicate) {
        if (!isPresent()) {
            return this;
        }
        return predicate.test(value) ? this : empty();
    }

    public <U> Optionality<U> map(Function<T, U> mapper) {
        if (!isPresent()) {
            return empty();
        }
        return Optionality.optionalOfNullable(mapper.apply(value));
    }

    public <U> Optionality<U> flatMap(Function<T, Optionality<U>> mapper) {
        if (!isPresent()) {
            return empty();
        }
        return mapper.apply(value);
    }

    public Optional<T> convert() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Optionality<?> that = (Optionality<?>) o;
        return new EqualsBuilder().append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).toHashCode();
    }

    @Override
    public String toString() {
        if (!isPresent()) {
            return "Optionality.empty";
        }
        return "Optionality{" +
                "value=" + value +
                '}';
    }
}
