package ru.crazylegend.focus.database.quiet;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public abstract class AbstractQuietExecutor {

    protected ThrowableHandler throwableHandler;
    protected ExecutorService asyncExecutorService;
    protected ExecutorService queueExecutorService;

    protected AbstractQuietExecutor() {
    }

    @Deprecated
    protected AbstractQuietExecutor(@NotNull ThrowableHandler throwableHandler) {
        this.throwableHandler = throwableHandler;
        useCachedThreadPoolAsyncExecutor();
    }

    protected AbstractQuietExecutor useThrowableHandler(@NotNull ThrowableHandler throwableHandler) {
        this.throwableHandler = throwableHandler;
        return this;
    }

    protected AbstractQuietExecutor useDatabaseThrowableHandler(@NotNull Logger logger) {
        return useThrowableHandler(ThrowableHandler.createForDatabase(logger));
    }

    protected AbstractQuietExecutor useDatabaseThrowableHandler(@NotNull Plugin plugin) {
        return useThrowableHandler(ThrowableHandler.createForDatabase(plugin));
    }

    protected AbstractQuietExecutor useAsyncExecutor(@NotNull ExecutorService executorService) {
        this.asyncExecutorService = executorService;
        return this;
    }

    protected AbstractQuietExecutor useCachedThreadPoolAsyncExecutor() {
        return useAsyncExecutor(Executors.newCachedThreadPool());
    }

    protected AbstractQuietExecutor useFixedThreadPoolAsyncExecutor(int threadsNumber) {
        return useAsyncExecutor(Executors.newFixedThreadPool(threadsNumber));
    }

    protected AbstractQuietExecutor useQueueExecutor(@NotNull ExecutorService executorService) {
        this.queueExecutorService = executorService;
        return this;
    }

    protected AbstractQuietExecutor useSingleThreadQueueExecutor() {
        return useQueueExecutor(Executors.newSingleThreadExecutor());
    }

    public void shutdown() {
        if (asyncExecutorService != null)
            asyncExecutorService.shutdown();
        if (queueExecutorService != null)
            queueExecutorService.shutdown();
    }

    protected void submitQuietlyAsync(Runnable task) {
        if (asyncExecutorService == null)
            throw new IllegalStateException("async executor service hasn't been initialized!");

        try {
            asyncExecutorService.submit(task).get();
        } catch (InterruptedException ignored) {
        } catch (Throwable ex) {
            if (throwableHandler != null)
                throwableHandler.handle(ex);
        }
    }

    protected void submitQuietlyQueue(@NotNull Runnable task) {
        if (queueExecutorService == null)
            throw new IllegalStateException("queue executor service hasn't been initialized!");

        try {
            queueExecutorService.submit(task).get();
        } catch (InterruptedException ignored) {
        } catch (Throwable ex) {
            if (throwableHandler != null)
                throwableHandler.handle(ex);
        }
    }

    protected CompletableFuture<Void> runQuietlyAsync(ThrowableRunnable runnable) {
        return CompletableFuture.runAsync(() -> runQuietly(runnable), this::submitQuietlyAsync);
    }

    protected CompletableFuture<Void> runQuietlyQueue(ThrowableRunnable runnable) {
        return CompletableFuture.runAsync(() -> runQuietly(runnable), this::submitQuietlyQueue);
    }

    protected void runQuietly(ThrowableRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            if (throwableHandler != null)
                throwableHandler.handle(throwable);
        }
    }

    protected <T> CompletableFuture<T> supplyQuietlyAsync(ThrowableSupplier<T> supplier) {
        return CompletableFuture.supplyAsync(() -> supplyQuietly(supplier), this::submitQuietlyAsync);
    }

    protected <T> CompletableFuture<T> supplyQuietlyQueue(ThrowableSupplier<T> supplier) {
        return CompletableFuture.supplyAsync(() -> supplyQuietly(supplier), this::submitQuietlyQueue);
    }

    protected <T> T supplyQuietly(ThrowableSupplier<T> supplier) {
        try {
            return supplier.supply();
        } catch (Throwable throwable) {
            if (throwableHandler != null)
                throwableHandler.handle(throwable);
            return null;
        }
    }

}
