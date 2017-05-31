package com.jesus_crie.hunhowex.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;

public class EmbedMessageBuilder {

    protected EmbedBuilder builder;
    protected StringBuilder content;

    public EmbedMessageBuilder(User u) {
        builder = new EmbedBuilder();
        builder.setFooter("Requested by " + CommandUtils.getUserString(u), u.getEffectiveAvatarUrl());
        builder.setColor(Color.WHITE);
        content = new StringBuilder();
    }

    public void setColor(Color c) {
        builder.setColor(c);
    }

    public void setTitleWithIcon(String title, String iconURL) {
        builder.setAuthor(title, null, iconURL);
    }

    public void setTitle(String title) {
        builder.setTitle(title, null);
    }

    public void setThumbnail(String thumbUrl) {
        builder.setThumbnail(thumbUrl);
    }

    public void setImage(String url) {
        builder.setImage(url);
    }

    public void addSection(String title, String content) {
        this.content.append("**" + title + ":** " + content + "\n");
    }

    public void addSection(String raw) {
        this.content.append(raw + "\n");
    }

    public void addRegularSection(String name, String content, boolean inline) {
        builder.addField(name, content, inline);
    }

    public void clearSections() {
        this.content = new StringBuilder();
    }

    public MessageEmbed build() {
        builder.setDescription(content);
        return builder.build();
    }
}
