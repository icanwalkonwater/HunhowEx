package com.jesus_crie.hunhowex.hooks;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public abstract class Hookable {

    protected String id;
    protected Message message;

    public String getId() {
        return id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message m) {
        message = m;
    }

    public abstract MessageEmbed serialize();
}
