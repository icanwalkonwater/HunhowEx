package com.jesus_crie.hunhowex;

import com.fasterxml.jackson.databind.JsonNode;
import com.jesus_crie.hunhowex.command.Command;
import com.jesus_crie.hunhowex.hooks.HookManager;
import com.jesus_crie.hunhowex.music.MusicManager;
import com.jesus_crie.hunhowex.storage.GuildConfig;
import com.jesus_crie.hunhowex.storage.JsonConfig;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HunhowExAPI {

    private static List<Command> commands;
    private static List<Command> cmdsPublic;
    private static boolean debug = false;
    private static MusicManager musicManager;
    private static HookManager hookManager;
    private static long start;

    public static void init() {
        start = System.currentTimeMillis();

        commands = new ArrayList<>();
        cmdsPublic = new ArrayList<>();
        start = System.currentTimeMillis();
        //hookManager = new HookManager(JsonConfig.getGuildConfigs());
    }

    // MISC

    public static String getUptime() {
        long uptime = System.currentTimeMillis() - start;
        return DurationFormatUtils.formatDuration(uptime, "d'd' HH'h' mm'm'");
    }

    // COMMANDS

    public static void registerCommands(Command... cmds) {
        commands.addAll(Arrays.asList(cmds));
        commands.forEach(c -> {
            if (!c.getInfos().isHidden())
                cmdsPublic.add(c);
        });

    }

    public static Command getCommand(String name) {
        name = name.toLowerCase();
        for (Command c : commands)
            if (c.getInfos().getAlisases().contains(name))
                return c;
        return null;
    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static List<Command> getCommandsPublic(String guild) {
        return Arrays.asList(cmdsPublic.stream().filter(c -> {
            if (!c.getInfos().isServerOnly())
                return true;
            return c.getInfos().getServerOnly().contains(guild);
        }).toArray(Command[]::new));
    }

    // LISTENERS

    public static void registerListener(ListenerAdapter listener) {
        HunhowEx.getInstance().getJda().addEventListener(listener);
    }

    public static void unregisterListener(ListenerAdapter listener) {
        HunhowEx.getInstance().getJda().removeEventListener(listener);
    }

    // JDA

    public static JDA getJda() {
        return HunhowEx.getInstance().getJda();
    }

    // STORAGE

    public static boolean isGuildRegistered(String guildId) {
        return getGuildPreferences(guildId) != null;
    }

    public static GuildConfig getGuildPreferences(String id) {
        return JsonConfig.getGuildConfig(id);
    }

    public static boolean registerGuild(GuildConfig cfg) {
        if (JsonConfig.saveGuildConfig(cfg)) {
            musicManager.registerGuild(cfg);
            return true;
        }
        return false;
    }

    public static void unregisterGuild(Guild guild) {
        musicManager.getMusicManager(guild).disconnect();
        JsonConfig.deleteGuildConfig(guild.getId());
    }

    // DEBUG

    public static boolean isDebugEnable() {
        return debug;
    }

    public static void enableDebug(boolean enable) {
        debug = enable;
    }

    // MUSIC

    public static MusicManager getMusicManager() {
        if (musicManager == null)
            musicManager = new MusicManager();
        return musicManager;
    }

    // HOOKS

    public static HookManager getHookManager() {
        return hookManager;
    }
}
