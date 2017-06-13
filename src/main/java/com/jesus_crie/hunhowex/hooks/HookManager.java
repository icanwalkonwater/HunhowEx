package com.jesus_crie.hunhowex.hooks;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.storage.GuildConfig;
import com.jesus_crie.hunhowex.warframe.WarframeAlert;
import com.jesus_crie.hunhowex.warframe.WarframeBaro;
import com.jesus_crie.hunhowex.warframe.WarframeSortie;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class HookManager {

    private HashMap<String, ChannelHook> hooks = new HashMap<>();

    public HookManager(List<GuildConfig> cfgs) {
        cfgs.forEach(cfg ->
            cfg.provideHooks().forEach(hook -> {
                TextChannel channel = HunhowExAPI.getJda().getTextChannelById(hook.get("channel").asText());
                switch (hook.get("type").asText()) {
                    case "general.logs":
                        registerHook(channel, GeneralLogs.class);
                        break;
                    case "dev.test":
                        registerHook(channel, DevTest.class);
                        break;
                    case "warframe.alert":
                        registerHook(channel, WarframeAlert.class);
                        break;
                    case "warframe.sortie":
                        registerHook(channel, WarframeSortie.class);
                        break;
                    case "warframe.baro":
                        registerHook(channel, WarframeBaro.class);
                        break;
                    default:
                        break;
                }
            })
        );
    }

    public ChannelHook getHookForChannel(TextChannel channel) {
        return hooks.get(channel.getId());
    }

    public List<ChannelHook> getHooksForGuild(String id) {
        return hooks.values()
                .stream()
                .filter(hook -> hook.getHooked().getGuild().getId().equals(id))
                .collect(Collectors.toList());
    }

    public void deleteHookForChannel(TextChannel channel) {
        hooks.remove(channel.getId());
    }

    public boolean hookExistForChannel(TextChannel channel) {
        return getHookForChannel(channel) != null;
    }

    public ChannelHook registerHook(TextChannel channel, Class type) {
        if (hookExistForChannel(channel))
            throw new UnsupportedOperationException("A hook already exist for this channel !");

        hooks.put(channel.getId(), new ChannelHook<>(channel, type));
        return getHookForChannel(channel);
    }
}
