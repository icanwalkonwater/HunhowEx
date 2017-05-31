package com.jesus_crie.hunhowex.listener;

import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.function.Consumer;

public class ReactionListener extends ListenerAdapter {

    private String messageId;
    private Consumer<MessageReactionAddEvent> action;

    public ReactionListener(String id, Consumer<MessageReactionAddEvent> action) {
        messageId = id;
        this.action = action;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (e.getReaction().isSelf())
            return;
        if (e.getMessageId().equalsIgnoreCase(messageId))
            action.accept(e);
    }
}
