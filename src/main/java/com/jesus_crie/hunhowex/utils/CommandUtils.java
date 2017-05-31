package com.jesus_crie.hunhowex.utils;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CommandUtils {

    public static final String PREFIX = "$";

    public static final String ICON_INFO = "https://cdn.discordapp.com/attachments/302785106802638848/302790538627776512/sign-info-icon.png";
    public static final String ICON_MUSIC = "https://cdn.discordapp.com/attachments/302785106802638848/318025666199027712/sound-3-icon.png";
    public static final String ICON_HELP = "https://cdn.discordapp.com/attachments/302785106802638848/302793019323580416/sign-question-icon.png";
    public static final String ICON_BED = "https://cdn.discordapp.com/attachments/302785106802638848/302814485440102403/hospital-bed-icon.png";
    public static final String ICON_ERROR = "https://cdn.discordapp.com/attachments/302785106802638848/303136843153539082/sign-error-icon.png";
    public static final String ICON_CHECK = "https://cdn.discordapp.com/attachments/302785106802638848/317008503392829443/sign-check-icon.png";
    public static final String ICON_TERMINAL = "https://cdn.discordapp.com/attachments/302785106802638848/317074381656424459/terminal-icon.png";
    public static final String ICON_DOOR = "https://cdn.discordapp.com/attachments/302785106802638848/317280450811002880/door-icon.png";

    public static final String EMOTE_PREVIOUS = "\u23EA";
    public static final String EMOTE_NEXT = "\u23E9";

    public static final String EMOTE_LITERAL_CHECKMARK = ":white_check_mark:";
    public static final String EMOTE_LITERAL_CROSS = ":no_entry:";

    public static final String GIF_BANNED = "http://i.imgur.com/O3DHIA5.gif";

    public static final String MUSIC_DEFAULT_PLAYLIST = "https://www.youtube.com/playlist?list=PL_aqGRnZqeg5l8f8P61SboaCweuSapHEk";

    public static final String GIPHY_BASE_GIF = "http://api.giphy.com/v1/gifs/";
    public static final String GIPHY_KEY = "dc6zaTOxFJmzC";

    public static String getUserString(User u) {
        return (u.getName() + "#" + u.getDiscriminator());
    }

    public static boolean isId(String id) {
        if (id.length() != 18)
            return false;
        try {
            Long.parseLong(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String stringifyDate(OffsetDateTime date) {
        date = date.withOffsetSameInstant(ZoneOffset.ofHours(2));
        return (date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : date.getDayOfMonth()) + "/"
                + (date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue()) + "/" + date.getYear() + " "
                + (date.getHour() < 10 ? "0" + date.getHour() : date.getHour()) + ":"
                + (date.getMinute() < 10 ? "0" + date.getMinute() : date.getMinute());
    }

    public static String stringifyDate(long timestamp) {
        return stringifyDate(OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getTimeZone("GMT").toZoneId()));
    }

    public static MessageEmbed getMessageGuildNotRegistered(User u) {
        EmbedMessageBuilder builder = new EmbedMessageBuilder(u);
        builder.setColor(Color.RED);
        builder.setTitleWithIcon("You need to register this server before using commands !", ICON_ERROR);
        builder.addSection("You need to use `" + PREFIX + "init` first !");

        return builder.build();
    }

    public static MessageEmbed getMessagePrivateChannel(User u) {
        EmbedMessageBuilder builder = new EmbedMessageBuilder(u);
        builder.setColor(Color.RED);
        builder.setTitleWithIcon("You can't use me in a private channel !", ICON_ERROR);

        return builder.build();
    }

    public static MessageEmbed getMessageError(User u, String msg) {
        EmbedMessageBuilder builder = new EmbedMessageBuilder(u);
        builder.setColor(Color.RED);
        builder.setTitleWithIcon(msg, ICON_ERROR);

        return builder.build();
    }
}
