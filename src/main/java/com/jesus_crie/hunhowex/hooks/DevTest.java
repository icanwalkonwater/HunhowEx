package com.jesus_crie.hunhowex.hooks;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class DevTest extends Hookable {

    private String content;

    public DevTest(String id, String content) {
        super.id = id;
        this.content = content;
    }

    public static String getType() {
        return "dev.test";
    }

    @Override
    public MessageEmbed serialize() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setFooter(getId(), null);
        builder.setDescription(content);

        return builder.build();
    }
}
