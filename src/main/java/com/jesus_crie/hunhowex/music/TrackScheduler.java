package com.jesus_crie.hunhowex.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TrackScheduler extends AudioEventAdapter {

    private Random random;

    private AudioPlayer player;
    private LinkedList<AudioTrack> queue;
    private List<AudioTrack> auto;

    public TrackScheduler(AudioPlayer player, List<AudioTrack> auto) {
        this.player = player;
        this.auto = auto;
        queue = new LinkedList<>();
        random = new Random();
    }

    public void queue(AudioTrack track) {
        queue.add(track);
    }

    public void nextTrack() {
        if (queue.isEmpty())
            queue.add(getAutoTrack());
        player.startTrack(queue.poll().makeClone(), false);
    }

    public void pausePlayer() {
        player.setPaused(true);
    }

    public void resumePlayer() {
        if (player.getPlayingTrack() == null)
            nextTrack();
        player.setPaused(false);
    }

    public void clearPlayer() {
        player.destroy();
        queue.clear();
    }

    @SuppressWarnings("unchecked")
    public LinkedList<AudioTrack> getQueue() {
        return (LinkedList<AudioTrack>) queue.clone();
    }

    public void randomizeQueue() {
        if (!queue.isEmpty())
            Collections.shuffle(queue);
    }

    private AudioTrack getAutoTrack() {
        return auto.get(random.nextInt(auto.size()));
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext)
            nextTrack();
    }
}
