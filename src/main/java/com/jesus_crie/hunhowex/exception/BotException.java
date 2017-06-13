package com.jesus_crie.hunhowex.exception;

import com.jesus_crie.hunhowex.logger.Logger;

public class BotException {

    public BotException(ExceptionGravity gravity, String message) {
        Logger.error("[" + gravity + "] " + message);
    }

    public BotException(ExceptionGravity gravity, String message, Class from) {
        Logger.error("[" + gravity + "] " + from.getSimpleName() + ": " + message);
    }

    public BotException(ExceptionGravity gravity, String message, Throwable e) {
        Logger.errorUnhandled("[" + gravity + "] " + message, e);
    }
}
