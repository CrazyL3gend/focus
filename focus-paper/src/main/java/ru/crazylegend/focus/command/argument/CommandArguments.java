package ru.crazylegend.focus.command.argument;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Presents the command arguments which also can be parameterized
 */
public interface CommandArguments extends Parameterized {

    /**
     * Returns the list of arguments
     *
     * @return the list of arguments
     */
    List<String> getArguments();

    /**
     * Returns the list of raw arguments which were specified by a command sender
     *
     * @return the list of raw arguments
     */
    List<String> getRaw();

    @NotNull DispatchPath getDispatchPath();

    /**
     * Returns the number of arguments
     *
     * @return the number of arguments
     */
    default int size() {
        return getArguments().size();
    }

    /**
     * Checks if this instance has no arguments
     *
     * @return 'true' if no arguments here or 'false' overwise
     */
    default boolean isEmpty() {
        return getArguments().isEmpty();
    }

    /**
     * Finds a first index of this element
     *
     * @param element Target element to find a first index
     * @return Found index or -1 overwise
     */
    default int indexOf(String element) {
        return getArguments().indexOf(element);
    }

    /**
     * Finds a last index of this element
     *
     * @param element Target element to find a last index
     * @return Found index or -1 overwise
     */
    default int lastIndexOf(String element) {
        return getArguments().lastIndexOf(element);
    }

    /**
     * Returns the stream of string arguments
     *
     * @return the stream of string arguments
     */
    Stream<String> stream();

    /**
     * Removes all elements by specified filter
     *
     * @param filter the filtering predicate
     * @return 'true' if any elements were removed or 'false' overwise
     */
    boolean removeIf(Predicate<String> filter);

    /**
     * Joins all arguments to one string using this delimiter
     *
     * @param delimiter delimiter for arguments
     * @return joined by delimiter arguments as string
     */
    default String join(CharSequence delimiter) {
        return String.join(delimiter, getArguments());
    }

    /**
     * Joins all arguments to one string using one space
     *
     * @return joined by space arguments as string
     */
    default String join() {
        return join(" ");
    }

    /**
     * Gets argument by index (starts from 0)
     *
     * @param index Index of target argument
     * @return The existing argument or null if it's not exist
     */
    default String get(int index) {
        return hasArgument(index) ? getArguments().get(index) : null;
    }

    /**
     * Gets argument by index (starts from 0) and tries to parse double from him
     *
     * @param index Index of target argument
     * @return The existing argument as double or -1 if it's not exist
     */
    double getAsDouble(int index);

    /**
     * Gets argument by index (starts from 0) and tries to parse float from him
     *
     * @param index Index of target argument
     * @return The existing argument as float or -1 if it's not exist
     */
    float getAsFloat(int index);

    /**
     * Gets argument by index (starts from 0) and tries to parse integer from him
     *
     * @param index Index of target argument
     * @return The existing argument as integer or -1 if it's not exist
     */
    int getAsInteger(int index);

    /**
     * Gets argument by index (starts from 0) and tries to parse long from him
     *
     * @param index Index of target argument
     * @return The existing argument as long or -1 if it's not exist
     * @since 1.12.0
     */
    long getAsLong(int index);

    /**
     * Removes argument by index (starts from 0) and returns removed item
     *
     * @param index Index of target argument
     * @return The removed argument as string
     */
    String remove(int index);

    /**
     * Removed argument from the arguments list
     *
     * @param element An element to remove from the list
     * @return 'true' if this element actually has been removed or 'false' overwise
     */
    boolean remove(Object element);

    /**
     * Removes argument by index (starts from 0) and tries to parse double from removed value
     *
     * @param index Index of target argument
     * @return The removed argument as double or -1 if it's not exist
     */
    double removeDouble(int index);

    /**
     * Removes argument by index (starts from 0) and tries to parse float from removed value
     *
     * @param index Index of target argument
     * @return The removed argument as float or -1 if it's not exist
     */
    float removeFloat(int index);

    /**
     * Removes argument by index (starts from 0) and tries to parse integer from removed value
     *
     * @param index Index of target argument
     * @return The removed argument as integer or -1 if it's not exist
     */
    int removeInteger(int index);

    /**
     * Removes argument by index (starts from 0) and tries to parse long from removed value
     *
     * @param index Index of target argument
     * @return The removed argument as long or -1 if it's not exist
     * @since 1.12.0
     */
    long removeLong(int index);

    /**
     * Checks if argument with specified index is exists
     *
     * @param index Index of target argument
     * @return 'true' if this argument is exists or 'false' if not
     */
    default boolean hasArgument(int index) {
        return size() > index;
    }

}
