package com.jesus_crie.hunhowex.listener;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.music.GuildMusicManager;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class VoiceListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        execute(e.getChannelLeft());
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        execute(e.getChannelJoined());
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        execute(e.getChannelLeft());
        execute(e.getChannelJoined());
    }

    private void execute(VoiceChannel channel) {
        GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(channel.getGuild());

        if (manager.isConnected() && channel.getId().equalsIgnoreCase(channel.getGuild().getAudioManager().getConnectedChannel().getId())) {
            if (channel.getMembers().size() <= 1) {
                Logger.info("[Music] [Listener] [" + channel.getGuild().getName() + "] Auto pausing");
                manager.setPaused(true);
            } else if (channel.getMembers().size() == 2) {
                Logger.info("[Music] [Listener] [" + channel.getGuild().getName() + "] Auto resuming");
                manager.setPaused(false);
            }
        }
    }
}
