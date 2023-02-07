package ru.crazylegend.focus.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.database.model.DataEntry;
import ru.crazylegend.focus.database.quiet.AbstractQuietExecutor;
import ru.crazylegend.focus.util.tool.CollectionsTool;
import ru.crazylegend.focus.util.tool.Validate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataRegistryManager extends AbstractQuietExecutor {

    private final Dao<DataEntry, String> dataEntriesDao;

    public DataRegistryManager(@NotNull Plugin plugin, @NotNull ConnectionSource connection) throws SQLException {
        this.dataEntriesDao = DaoManager.createDao(connection, DataEntry.class);
        TableUtils.createTableIfNotExists(connection, DataEntry.class);

        super.useCachedThreadPoolAsyncExecutor();
        super.useDatabaseThrowableHandler(plugin);
    }

    public @NotNull CompletableFuture<List<DataEntry>> getAllEntries() {
        return supplyQuietlyAsync(dataEntriesDao::queryForAll);
    }

    public @NotNull CompletableFuture<Map<String, String>> getEntriesMap() {
        return getAllEntries().thenApply(list -> CollectionsTool.getMapFromList(list, DataEntry::getKey, DataEntry::getValue));
    }

    public @NotNull CompletableFuture<DataEntry> getEntry(@NotNull String key) {
        Validate.notEmpty(key, "key");
        return supplyQuietlyAsync(() -> dataEntriesDao.queryForId(key));
    }

    public @NotNull CompletableFuture<DataEntry> getEntryOrDefault(@NotNull String key, @NotNull Object value) {
        Validate.notEmpty(key, "key");
        Validate.notNull(value, "value");

        return getEntry(key).thenApply(dataEntry -> {
            if (dataEntry == null) {
                dataEntry = new DataEntry(key, value);
                saveEntry(dataEntry).join();
            }
            return dataEntry;
        });
    }

    public @NotNull CompletableFuture<Boolean> hasEntry(@NotNull String key) {
        Validate.notEmpty(key, "key");
        return supplyQuietlyAsync(() -> dataEntriesDao.idExists(key));
    }

    public @NotNull CompletableFuture<Void> removeEntry(@NotNull DataEntry dataEntry) {
        Validate.notNull(dataEntry, "dataEntry");
        return removeEntry(dataEntry.getKey());
    }

    public @NotNull CompletableFuture<Void> removeEntry(@NotNull String key) {
        Validate.notEmpty(key, "key");
        return runQuietlyAsync(() -> dataEntriesDao.deleteById(key));
    }

    public @NotNull CompletableFuture<Void> saveEntry(@NotNull DataEntry dataEntry) {
        Validate.notNull(dataEntry, "dataEntry");
        return runQuietlyAsync(() -> dataEntriesDao.createOrUpdate(dataEntry));
    }

    public @NotNull CompletableFuture<Void> saveEntry(@NotNull String key, @NotNull Object value) {
        Validate.notEmpty(key, "key");
        Validate.notNull(value, "value");
        return saveEntry(new DataEntry(key, value));
    }

}
