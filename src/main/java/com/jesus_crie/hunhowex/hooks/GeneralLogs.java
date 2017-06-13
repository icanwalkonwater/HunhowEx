package com.jesus_crie.hunhowex.hooks;

import net.dv8tion.jda.core.entities.MessageEmbed;

public class GeneralLogs extends Hookable {

    public static String getType() {
        return "general.logs";
    }

    @Override
    public MessageEmbed serialize() {
        return null;
    }
}
