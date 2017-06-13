package com.jesus_crie.hunhowex.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jesus_crie.hunhowex.HunhowEx;
import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.exception.BotException;
import com.jesus_crie.hunhowex.exception.ExceptionGravity;
import com.jesus_crie.hunhowex.logger.Logger;
import net.dv8tion.jda.core.entities.Guild;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonConfig {

    private static ObjectMapper mapper;
    private static File configFile;
    private static Timer timer;

    private static HashMap<String, GuildConfig> guildConfigs;

    public static boolean init(List<Guild> guildsId) {

        // Prepare mapper and file
        mapper = new ObjectMapper();
        timer = new Timer();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        configFile = new File("./config.json");
        if (!configFile.exists()) {
            Logger.info("[Config] No config file found, creating one.");
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                new BotException(ExceptionGravity.FATAL, "Can't create config file !", IOException.class);
                e.printStackTrace();
                return false;
            }
        }

        // Read and parse
        try {
            List<GuildConfig> cfgs = mapper.readValue(configFile, new TypeReference<List<GuildConfig>>() {});
            guildConfigs = new HashMap<>();
            cfgs.forEach(cfg -> guildConfigs.put(cfg.getGuildId(), cfg));
        } catch (IOException e) {
            new BotException(ExceptionGravity.FATAL, "Can't read config file !", IOException.class);
            e.printStackTrace();
            return false;
        }

        // Check
        List<String> ids = Arrays.asList(guildsId.stream().map(Guild::getId).toArray(String[]::new));

        guildConfigs.values().forEach(g -> {
            if (!ids.contains(g.getGuildId()))
                guildConfigs.remove(g);
        });

        // Auto save
        startTimer();

        return true;
    }

    private static boolean save() {
        try {
            mapper.writeValue(configFile, guildConfigs.values());
            return true;
        } catch (IOException e) {
            new BotException(ExceptionGravity.FATAL, "Can't write config !", IOException.class);
            return false;
        }
    }

    private static void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                save();
            }
        }, 30000, 30000);
    }

    public static void stopAutoSave() {
        timer.cancel();
        save();
    }

    public static List<GuildConfig> getGuildConfigs() {
        return new ArrayList<>(guildConfigs.values());
    }

    public static GuildConfig getGuildConfig(String id) {
        return guildConfigs.get(id);
    }

    public static boolean saveGuildConfig(GuildConfig cfg) {
        guildConfigs.put(cfg.getGuildId(), cfg);
        if (save())
            return init(HunhowExAPI.getJda().getGuilds());
        return false;
    }

    public static boolean deleteGuildConfig(String id) {
        guildConfigs.remove(id);
        if (save())
            return init(HunhowExAPI.getJda().getGuilds());
        return false;
    }
}
