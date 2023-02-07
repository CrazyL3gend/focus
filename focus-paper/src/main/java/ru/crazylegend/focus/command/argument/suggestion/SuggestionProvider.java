package ru.crazylegend.focus.command.argument.suggestion;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The provider of suggestions for the tab completion
 *
 * @since 1.12.0
 */
@FunctionalInterface
public interface SuggestionProvider {

    /**
     * Create suggestion provider using this stream supplier
     *
     * @param providerSupplier stream supplier to use
     * @return A new instance of {@link SuggestionProvider}
     * @deprecated Actually needs further consideration...
     */
    @Deprecated
    static SuggestionProvider ofStream(ProviderSupplier<Stream<String>> providerSupplier) {
        return new FilteringSuggestionsProvider(providerSupplier);
    }

    /**
     * Create suggestion provider using this stream
     *
     * @param staticStream static stream to use
     * @return A new instance of {@link SuggestionProvider}
     * @deprecated Actually needs further consideration...
     */
    @Deprecated
    static SuggestionProvider ofStream(Stream<String> staticStream) {
        return SuggestionProvider.ofStream((a, b) -> staticStream);
    }

    /**
     * Create suggestion provider using this collection supplier
     *
     * @param collectionSupplier collection supplier to use
     * @return A new instance of {@link SuggestionProvider}
     * @deprecated Actually needs further consideration...
     */
    @Deprecated
    static SuggestionProvider ofCollection(ProviderSupplier<Collection<String>> collectionSupplier) {
        return SuggestionProvider.ofStream((a, b) -> collectionSupplier.supply(a, b).stream());
    }

    /**
     * Create suggestion provider using this collection
     *
     * @param staticCollection static collection to use
     * @return A new instance of {@link SuggestionProvider}
     * @deprecated Actually needs further consideration...
     */
    @Deprecated
    static SuggestionProvider ofCollection(Collection<String> staticCollection) {
        return SuggestionProvider.ofStream((a, b) -> staticCollection.stream());
    }

    /**
     * Create suggestion provider using this iterable supplier
     *
     * @param iterableSupplier iterable supplier to use
     * @return A new instance of {@link SuggestionProvider}
     * @deprecated Actually needs further consideration...
     */
    @Deprecated
    static SuggestionProvider ofIterable(ProviderSupplier<Iterable<String>> iterableSupplier) {
        return SuggestionProvider.ofStream((a, b) -> StreamSupport.stream(iterableSupplier.supply(a, b).spliterator(), false));
    }

    /**
     * Create suggestion provider using this iterable
     *
     * @param staticIterable static iterable to use
     * @return A new instance of {@link SuggestionProvider}
     * @deprecated Actually needs further consideration...
     */
    @Deprecated
    static SuggestionProvider ofIterable(Iterable<String> staticIterable) {
        return SuggestionProvider.ofStream((a, b) -> StreamSupport.stream(staticIterable.spliterator(), false));
    }

    /**
     * Create suggestion provider using this values array
     *
     * @param values static values array to use
     * @return A new instance of {@link SuggestionProvider}
     * @deprecated Actually needs further consideration...
     */
    @Deprecated
    static SuggestionProvider of(String... values) {
        return SuggestionProvider.ofStream((a, b) -> Arrays.stream(values));
    }

    /**
     * Provide a list of suggestions using the command sender and his input
     *
     * @param sender the command sender
     * @param input  the sender's input after the '=' symbol
     * @return A list of suggestions (can be null)
     */
    List<String> provideSuggestions(CommandSender sender, String input);

}
