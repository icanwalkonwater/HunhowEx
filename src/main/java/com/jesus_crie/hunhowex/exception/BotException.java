package com.jesus_crie.hunhowex.exception;

import com.jesus_crie.hunhowex.logger.Logger;

public class BotException {

    public BotException(ExceptionGravity gravity, String message) {
        Logger.error("[" + gravity.toString() + "] " + message);
    }

    public BotException(ExceptionGravity gravity, String message, Class from) {
        Logger.error("[" + gravity.toString() + "] " + from.getSimpleName() + ": " + message);
    }

    public BotException(ExceptionGravity gravity, String message, Throwable e) {
        Logger.errorUnhandled("[" + gravity + "] " + message + " Message: " + e, e);
    }
}
