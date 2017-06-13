package com.jesus_crie.hunhowex.webhooks;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageType;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class WebhookMessageBuilder extends MessageBuilder {

    private String username;
    private String avatar_url;

    private List<MessageEmbed> embeds = new ArrayList<>();

    public WebhookMessageBuilder() {}

    public WebhookMessageBuilder(Message msg) {
        if (!msg.isWebhookMessage())
            return;
        setUsername(msg.getAuthor().getName());
        setAvatarUrl(msg.getAuthor().getEffectiveAvatarUrl());
        addEmbed(msg.getEmbeds());
        append(msg.getRawContent());
    }

    public WebhookMessageBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public WebhookMessageBuilder setAvatarUrl(String url) {
        this.avatar_url = url;
        return this;
    }

    public WebhookMessageBuilder addEmbed(MessageEmbed... embeds) {
        for (MessageEmbed e : embeds)
            this.embeds.add(e);

        return this;
    }

    public WebhookMessageBuilder addEmbed(List<MessageEmbed> embeds) {
        this.embeds.addAll(embeds);
        return this;
    }

    public WebhookMessageBuilder clearEmbeds() {
        embeds = new ArrayList<>();
        return this;
    }

    @Override
    public WebhookMessageBuilder append(CharSequence msg) {
        super.append(msg);
        return this;
    }

    @Override
    public boolean isEmpty() {
        return builder.length() == 0 && embeds.size() <= 0;
    }

    @Override
    public WebhookMessage build() {
        String message = builder.toString();
        if (this.isEmpty())
            throw new IllegalStateException("Cannot build a Message with no content. (You never added any content to the message)");
        if (message.length() > 2000)
            throw new IllegalStateException("Cannot build a Message with more than 2000 characters. Please limit your input.");

        return new WebhookMessage(-1, null, MessageType.DEFAULT).setContent(message).setTTS(isTTS)
                .setEmbeds(embeds).setUsername(username).setAvatarURL(avatar_url);
    }
}
