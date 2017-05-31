package com.jesus_crie.hunhowex.listener;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MuteListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        /**new Thread(() -> {
            if (e.getMember().getRoles().stream().filter(r -> r.getName().equalsIgnoreCase("mute")).findAny().get() != null) {
                e.getMessage().delete().complete();
            }
        }).start();**/
        // TODO
    }
}
