package com.jesus_crie.hunhowex.music;

import com.jesus_crie.hunhowex.logger.Logger;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.List;

public class GuildMusicManager {

    private Guild guild;
    private AudioPlayer player;
    private AutoPlaylist auto;
    private TrackScheduler scheduler;

    public GuildMusicManager(Guild guild, AudioPlayer player, AutoPlaylist auto) {
        this.guild = guild;
        this.player = player;
        this.auto = auto;
        scheduler = new TrackScheduler(player, auto.get());

        player.addListener(scheduler);
        player.setVolume(15);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));

        Logger.info("[Music] Manager for `" + guild.getName() + "` ready !");
    }

    public void connectToChannel(VoiceChannel channel) {
        if (!guild.getAudioManager().isAttemptingToConnect())
            guild.getAudioManager().openAudioConnection(channel);
        while(guild.getAudioManager().isConnected()) {}

        guild.getAudioManager().setSelfDeafened(true);
    }

    public void disconnect() {
        guild.getAudioManager().closeAudioConnection();
    }

    public boolean isConnected() {
        return guild.getAudioManager().isConnected();
    }

    public GuildVoiceState getUserVoiceState(User u) {
        return guild.getVoiceStates().stream()
                .filter(state -> state.getMember().getUser().getId().equalsIgnoreCase(u.getId()))
                .findFirst().get();
    }

    public void setPaused(boolean paused) {
        if (paused)
            scheduler.pausePlayer();
        else
            scheduler.resumePlayer();
    }

    public boolean isPaused() {
        return player.isPaused();
    }

    public void playTrack(AudioTrack track) {
        scheduler.queue(track);
    }

    public AudioTrack getCurrentTrack() {
        return player.getPlayingTrack();
    }

    public void skipTrack() {
        scheduler.nextTrack();
    }

    public void shuffle() {
        scheduler.randomizeQueue();
    }

    public List<AudioTrack> getQueue() {
        return scheduler.getQueue();
    }

    public void stop() {
        scheduler.clearPlayer();
    }

    public int getVolume() {
        return player.getVolume();
    }

    public void setVolume(int v) {
        player.setVolume(v);
    }
}
