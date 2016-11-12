package com.codingchili.core.Files;

import com.hazelcast.util.Preconditions;

import com.codingchili.core.Context.CoreContext;
import com.codingchili.core.Context.TimerSource;

/**
 * @author Robin Duda
 */
class FileWatcherBuilder {
    private final FileWatcher watcher;

    FileWatcherBuilder(CoreContext context) {
        this.watcher = new FileWatcher(context);
    }

    /**
     * Sets the listener that is called on file changes.
     * @param listener the listener to be used.
     */
    FileWatcherBuilder withListener(FileStoreListener listener) {
        watcher.setListener(listener);
        return this;
    }

    /**
     * Defines the directory that should be watched relative from the application root.
     * @param directory the directory to be watched, includes its subdirectories.
     */
    FileWatcherBuilder onDirectory(String directory) {
        watcher.setDirectory(directory);
        return this;
    }

    /**
     * The rate in which to poll the file change events.
     * @param rate a timersource in milliseconds.
     */
    FileWatcherBuilder rate(TimerSource rate) {
        watcher.setRate(rate);
        return this;
    }

    /**
     * Constructs the new FileWatcher.
     */
    void build() {
        Preconditions.checkNotNull(watcher.directory);
        Preconditions.checkNotNull(watcher.listener);
        Preconditions.checkNotNull(watcher.rate);
        watcher.initialize();
    }
}
