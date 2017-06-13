package com.jesus_crie.hunhowex.warframe;

import com.jesus_crie.hunhowex.hooks.Hookable;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class WarframeBaro extends Hookable {

    public static String getType() {
        return "warframe.baro";
    }

    @Override
    public MessageEmbed serialize() {
        return null;
    }
}
