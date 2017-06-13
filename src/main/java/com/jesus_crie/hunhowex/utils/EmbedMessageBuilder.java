package com.jesus_crie.hunhowex.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;

public class EmbedMessageBuilder extends EmbedBuilder {

    protected StringBuilder content;

    public EmbedMessageBuilder(User u) {
        super.setFooter("Requested by " + CommandUtils.getUserString(u), u.getEffectiveAvatarUrl());
        super.setColor(Color.WHITE);
        content = new StringBuilder();
    }

    public EmbedMessageBuilder setTitleWithIcon(String title, String iconURL) {
        super.setAuthor(title, null, iconURL);
        return this;
    }

    public EmbedMessageBuilder setTitle(String title) {
        super.setTitle(title, null);
        return this;
    }

    public EmbedMessageBuilder setThumbnail(String thumbUrl) {
        super.setThumbnail(thumbUrl);
        return this;
    }

    public EmbedMessageBuilder setImage(String url) {
        super.setImage(url);
        return this;
    }

    public EmbedMessageBuilder addSection(String title, String content) {
        this.content.append("**" + title + ":** " + content + "\n");
        return this;
    }

    public EmbedMessageBuilder addSection(String raw) {
        this.content.append(raw + "\n");
        return this;
    }


    public EmbedMessageBuilder clearSections() {
        this.content = new StringBuilder();
        return this;
    }

    public MessageEmbed build() {
        if (content.length() > 0)
            super.setDescription(content);
        return super.build();
    }
}
