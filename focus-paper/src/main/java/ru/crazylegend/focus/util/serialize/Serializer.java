package ru.crazylegend.focus.util.serialize;

public interface Serializer<T, R> {

    R serialize(T t);

    T deserialize(R r);

}
