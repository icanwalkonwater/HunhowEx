package com.jesus_crie.hunhowex.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GuildConfig {

    private String guildId;
    private String musicAutoPlaylist;
    private List<String> cmdUsable;

    @JsonCreator
    public GuildConfig(@JsonProperty("id") String guildId,
                       @JsonProperty("music_autoplaylist") String musicAutoPlaylist) {
        this.guildId = guildId;
        this.musicAutoPlaylist = musicAutoPlaylist;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getMusicAutoPlaylist() {
        return musicAutoPlaylist;
    }
}
