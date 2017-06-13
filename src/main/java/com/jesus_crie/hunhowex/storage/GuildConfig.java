package com.jesus_crie.hunhowex.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.hooks.ChannelHook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RENEMBER to change ConfigCommand
 */
@JsonSerialize(using = GuildConfig.GuildSerializer.class)
public class GuildConfig {

    private String guildId;
    private String musicAutoPlaylist;
    private String welcomeMessage;
    private String welcomeChannel;
    private List<JsonNode> hooks;

    private static GuildConfig defaultCfg = new GuildConfig("0",
            "default",
            "none",
            "{main}",
            new ArrayList<>());

    @JsonCreator
    public GuildConfig(@JsonProperty("id") String guildId,
                       @JsonProperty("music_autoplaylist") String musicAutoPlaylist,
                       @JsonProperty("welcome_message") String welcomeMessage,
                       @JsonProperty("welcome_channel") String welcomeChannel,
                       @JsonProperty("hooks") List<JsonNode> hooks) {
        this.guildId = guildId;
        this.musicAutoPlaylist = musicAutoPlaylist;
        this.welcomeMessage = welcomeMessage;
        this.welcomeChannel = welcomeChannel;
        if (hooks != null)
            this.hooks = hooks;
    }

    public static GuildConfig getDefault(String id) {
        return new GuildConfig(id,
                "default",
                "none",
                "{main}",
                new ArrayList<>());
    }

    public static GuildConfig getDefault() {
        return defaultCfg;
    }

    @Deprecated
    public List<JsonNode> provideHooks() {
        return hooks;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getMusicAutoPlaylist() {
        return musicAutoPlaylist;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public String getWelcomeChannel() {
        return welcomeChannel;
    }

    public void setMusicAutoPlaylist(String musicAutoPlaylist) {
        this.musicAutoPlaylist = musicAutoPlaylist;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public void setWelcomeChannel(String welcomeChannel) {
        this.welcomeChannel = welcomeChannel;
    }

    @Deprecated
    public static class GuildSerializer extends StdSerializer<GuildConfig> {

        public GuildSerializer() {
            this(null);
        }

        public GuildSerializer(Class<GuildConfig> t) {
            super(t);
        }

        @Override
        public void serialize(GuildConfig value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();

            gen.writeStringField("id", value.guildId);
            gen.writeStringField("music_autoplaylist", value.musicAutoPlaylist);
            gen.writeStringField("welcome_message", value.welcomeMessage);
            gen.writeStringField("welcome_channel", value.welcomeChannel);

            /*gen.writeArrayFieldStart("hooks");
            for (ChannelHook hook : HunhowExAPI.getHookManager().getHooksForGuild(value.guildId)) {
                gen.writeStartObject();
                gen.writeStringField("channel", hook.getHooked().getId());
                gen.writeStringField("type", hook.getType());
                gen.writeEndObject();
            }
            gen.writeEndArray();*/

            gen.writeEndObject();
        }
    }
}
