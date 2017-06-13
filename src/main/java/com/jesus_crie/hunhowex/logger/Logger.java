package com.jesus_crie.hunhowex.logger;

import com.jesus_crie.hunhowex.HunhowEx;
import com.jesus_crie.hunhowex.HunhowExAPI;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static MessageChannel channel;

    public static void setLogChannel(MessageChannel c) {
        channel = c;
    }

    public static void info(String message) {
        send("[Info] " + message, DiscordLog.LevelColor.NORMAL);
    }

    public static void error(String message) {
        send("[ERROR] " + message, DiscordLog.LevelColor.ERROR);
    }

    public static void errorUnhandled(String message, Throwable e) {
        if (HunhowEx.isConnected() && channel != null)
            channel.sendMessage(new DiscordLog.DiscordLogError(message, e.getStackTrace()).build()).queue();
    }

    private static void send(String content, DiscordLog.LevelColor level) {
        System.out.println(getDatePrefix() + " " + content);
        if (HunhowEx.isConnected() && channel != null)
            channel.sendMessage(new DiscordLog(level, content).build()).queue();
    }

    public static String getDatePrefix() {
        return "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "]";
    }
}
