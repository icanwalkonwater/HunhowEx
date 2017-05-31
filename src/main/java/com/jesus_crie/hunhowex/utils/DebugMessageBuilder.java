package com.jesus_crie.hunhowex.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class DebugMessageBuilder extends EmbedBuilder {

    public DebugMessageBuilder(Message msg) {
        super();
        super.setColor(Color.ORANGE);
        super.setAuthor("DEBUG", null, CommandUtils.ICON_TERMINAL);

        super.addField("Input", "```" + msg.getRawContent() + "```", false);
        super.addField("Message ID", msg.getId(), false);
        super.addField("Author", msg.getAuthor().getId() + "/" + msg.getAuthor().getAsMention(), false);
        super.addField("Sent on", CommandUtils.stringifyDate(msg.getCreationTime()), true);
        super.addField("Is TTS", String.valueOf(msg.isTTS()).toUpperCase(), true);
        super.addField("Is webhook", String.valueOf(msg.isWebhookMessage()).toUpperCase(), true);
        super.addField("Has attachments", String.valueOf(!msg.getAttachments().isEmpty()).toUpperCase(), true);
    }
}
