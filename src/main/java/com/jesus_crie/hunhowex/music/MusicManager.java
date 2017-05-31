package com.jesus_crie.hunhowex.music;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.exception.BotException;
import com.jesus_crie.hunhowex.exception.ExceptionGravity;
import com.jesus_crie.hunhowex.storage.GuildConfig;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class MusicManager {

    private AudioPlayerManager manager;
    private HashMap<String, GuildMusicManager> guildManagers;
    private AutoPlaylist defaultAuto;

    public MusicManager() {
        manager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(manager);
        AudioSourceManagers.registerRemoteSources(manager);

        guildManagers = new HashMap<>();
    }

    public void loadAutoPlaylist() {
        defaultAuto = new AutoPlaylist(CommandUtils.MUSIC_DEFAULT_PLAYLIST, manager);
    }

    public void loadAudioTrack(String identifier,
                               Consumer<AudioTrack> trackHandler,
                               Consumer<AudioPlaylist> playlistHandler,
                               Runnable noMatchHandler,
                               Runnable errorHandler) {

        synchronized (this) {
            try {
                manager.loadItem(identifier, new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        trackHandler.accept(track);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        playlistHandler.accept(playlist);
                    }

                    @Override
                    public void noMatches() {
                        noMatchHandler.run();
                    }

                    @Override
                    public void loadFailed(FriendlyException exception) {
                        errorHandler.run();
                    }
                }).get();
            } catch (Exception e) {
                new BotException(ExceptionGravity.ERROR, "Error while loading track `" + identifier + "`", e);
            }
        }
    }

    public GuildMusicManager getMusicManager(Guild g) {
        return guildManagers.get(g.getId());
    }

    public void registerGuilds(List<GuildConfig> guilds) {
        guilds.forEach(g ->
            guildManagers.put(g.getGuildId(),
                    new GuildMusicManager(HunhowExAPI.getJda().getGuildById(g.getGuildId()),
                    manager.createPlayer(),
                    g.getMusicAutoPlaylist().equalsIgnoreCase("default")
                            ? defaultAuto
                            : new AutoPlaylist(g.getMusicAutoPlaylist(), manager)))
        );
    }

    public void disconnectFromAll() {
        guildManagers.values().forEach(m -> m.disconnect());
    }
}
