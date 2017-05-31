package com.jesus_crie.hunhowex.logger;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class DiscordLog extends EmbedBuilder {

    public DiscordLog(LevelColor color, String message) {
        super();
        super.setColor(color.get());
        super.setAuthor(Logger.getDatePrefix(), null, color.getUrl());
        super.setDescription(message);
    }

    public enum LevelColor {
        NORMAL(Color.GREEN, CommandUtils.ICON_CHECK),
        ERROR(Color.RED, CommandUtils.ICON_ERROR);

        private Color c;
        private String url;
        LevelColor(Color c, String url) {
            this.c = c;
            this.url = url;
        }

        public Color get() {
            return c;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class DiscordLogError extends EmbedBuilder {

        public DiscordLogError(String message, StackTraceElement[] stackTrace) {
            super();
            super.setColor(LevelColor.ERROR.get());
            super.setAuthor(Logger.getDatePrefix(), null, LevelColor.ERROR.getUrl());

            List<StackTraceElement> trace = Arrays.asList(stackTrace);
            super.setDescription(message + "\n\n" + String.join("\n", trace.stream().map(t -> t.toString()).toArray(String[]::new)));
        }
    }
}
