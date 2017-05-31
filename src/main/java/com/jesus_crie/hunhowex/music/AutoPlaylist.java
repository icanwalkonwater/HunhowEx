package com.jesus_crie.hunhowex.music;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.logger.Logger;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;
import java.util.List;

public class AutoPlaylist {

    private List<AudioTrack> tracks;

    public AutoPlaylist(String playlist, AudioPlayerManager manager) {
        tracks = new ArrayList<>();

        HunhowExAPI.getMusicManager().loadAudioTrack(playlist,
                track -> tracks.add(track),
                play -> tracks.addAll(play.getTracks()),
                () -> Logger.error("[Music] No matches for `" + playlist + "`"),
                () -> Logger.error("[Music] Failed to load autoplaylist `" + playlist + "`"));
    }

    public List<AudioTrack> get() {
        return tracks;
    }
}
