package com.jesus_crie.hunhowex.utils;

import com.jesus_crie.hunhowex.listener.ReactionListener;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.function.Consumer;

public abstract class ReactableMessageBuilder extends EmbedMessageBuilder {

    protected ReactionListener listener;
    protected Consumer<MessageReactionAddEvent> action;
    protected String[] emotes;

    public ReactableMessageBuilder(User u) {
        super(u);
    }

    public void setReactions(String... emotes) {
        this.emotes = emotes;
    }

    public abstract void send(MessageChannel channel);
}
