package com.jesus_crie.hunhowex.webhooks;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.core.entities.impl.MessageImpl;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class WebhookMessage extends MessageImpl {

    private String username;
    private String avatar_url;

    public WebhookMessage(long id, MessageChannel channel, MessageType type) {
        super(id, channel, true, type);
    }

    public WebhookMessage(Message msg) {
        super(msg.getIdLong(), msg.getChannel(), true, MessageType.DEFAULT);
        super.setAttachments(msg.getAttachments());
        super.setAuthor(msg.getAuthor());
        super.setContent(msg.getRawContent());
        super.setEditedTime(msg.getEditedTime());
        super.setEmbeds(msg.getEmbeds());
        super.setMentionedChannels(msg.getMentionedChannels());
        super.setMentionedRoles(msg.getMentionedRoles());
        super.setMentionedUsers(msg.getMentionedUsers());
        super.setMentionsEveryone(msg.mentionsEveryone());
        super.setPinned(msg.isPinned());
        super.setReactions(msg.getReactions());
        super.setTime(msg.getCreationTime());
        super.setTTS(msg.isTTS());
    }

    public WebhookMessage setUsername(String username) {
        this.username = username;
        return this;
    }

    public WebhookMessage setAvatarURL(String avatar_url) {
        this.avatar_url = avatar_url;
        return this;
    }

    @Override
    public WebhookMessage setPinned(boolean pinned) {
        super.setPinned(pinned);
        return this;
    }

    @Override
    public WebhookMessage setMentionedUsers(List<User> mentionedUsers) {
        super.setMentionedUsers(mentionedUsers);
        return this;
    }

    @Override
    public WebhookMessage setMentionedChannels(List<TextChannel> mentionedChannels) {
        super.setMentionedChannels(mentionedChannels);
        return this;
    }

    @Override
    public WebhookMessage setMentionedRoles(List<Role> mentionedRoles) {
        super.setMentionedRoles(mentionedRoles);
        return this;
    }

    @Override
    public WebhookMessage setMentionsEveryone(boolean mentionsEveryone) {
        super.setMentionsEveryone(mentionsEveryone);
        return this;
    }

    @Override
    public WebhookMessage setTTS(boolean TTS) {
        super.setTTS(TTS);
        return this;
    }

    @Override
    public WebhookMessage setTime(OffsetDateTime time) {
        super.setTime(time);
        return this;
    }

    @Override
    public WebhookMessage setEditedTime(OffsetDateTime editedTime) {
        super.setEditedTime(editedTime);
        return this;
    }

    @Override
    public WebhookMessage setAuthor(User author) {
        super.setAuthor(author);
        return this;
    }

    @Override
    public WebhookMessage setContent(String content) {
        super.setContent(content);
        return this;
    }

    @Override
    public WebhookMessage setAttachments(List<Attachment> attachments) {
        super.setAttachments(attachments);
        return this;
    }

    @Override
    public WebhookMessage setEmbeds(List<MessageEmbed> embeds) {
        super.setEmbeds(embeds);
        return this;
    }

    @Override
    public WebhookMessage setReactions(List<MessageReaction> reactions) {
        super.setReactions(reactions);
        return this;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("content", getRawContent());
        obj.put("tts", isTTS());
        obj.put("username", username);
        obj.put("avatar_url", avatar_url);

        if (!getEmbeds().isEmpty())
            obj.put("embeds", getEmbeds()
                    .stream()
                    .map(e -> (MessageEmbedImpl) e)
                    .map(MessageEmbedImpl::toJSONObject)
                    .collect(Collectors.toList()));

        return obj;
    }
}
