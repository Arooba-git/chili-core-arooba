package com.codingchili.core.Testing;

import com.codingchili.core.Logging.*;

/**
 * @author Robin Duda
 */
public class LoggerMock extends ConsoleLogger {
    private MockLogListener listener;

    public LoggerMock(MockLogListener listener) {
        this.listener = listener;
    }

    @Override
    public Logger log(String line) {
        listener.onLogged(line);
        return this;
    }

    @Override
    public Logger log(String line, Level level) {
        listener.onLogged(line);
        return this;
    }
}