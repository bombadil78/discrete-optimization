package ch.bullsoft.knapsack.util;

import static ch.bullsoft.knapsack.util.Logger.LogLevel.DEBUG;
import static ch.bullsoft.knapsack.util.Logger.LogLevel.ERROR;
import static ch.bullsoft.knapsack.util.Logger.LogLevel.INFO;

public final class Logger {

    private static LogLevel logLevel = LogLevel.NONE;

    public enum LogLevel {
        DEBUG, INFO, ERROR, NONE
    }

    private final String className;

    private Logger(String className) {
        this.className = className;
    }

    public static <T> Logger createLogger(Class<T> clazz) {
        return new Logger(clazz.getName());
    }

    public void error(String msg) {
        printConditionally(ERROR, msg);
    }

    public void info(String msg) {
        printConditionally(INFO, msg);
    }

    public void debug(String msg) {
        printConditionally(DEBUG, msg);
    }

    private void printConditionally(LogLevel printLogLevel, String msg) {
        if (printLogLevel.ordinal() >= logLevel.ordinal()) {
            System.out.println(msg);
        }
    }
}
