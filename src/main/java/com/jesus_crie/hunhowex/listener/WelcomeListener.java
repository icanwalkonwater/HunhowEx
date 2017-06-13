package com.jesus_crie.hunhowex.listener;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class WelcomeListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        String msg = HunhowExAPI.getGuildPreferences(e.getGuild().getId()).getWelcomeMessage();
        String chan = HunhowExAPI.getGuildPreferences(e.getGuild().getId()).getWelcomeChannel();

        if (!msg.equalsIgnoreCase("none")) {
            Logger.info("[" + e.getGuild().getName() + "] Welcome to " + CommandUtils.getUserString(e.getMember().getUser()));
            if (!e.getGuild().getTextChannelsByName(chan, true).isEmpty())
                e.getGuild().getTextChannelsByName(chan, true).get(0).sendMessage(msg.replace("{user}", e.getMember().getAsMention())).queue();
            else if (chan.equalsIgnoreCase("{main}"))
                e.getGuild().getPublicChannel().sendMessage(msg.replace("{user}", e.getMember().getAsMention())).queue();
        }
    }
}
