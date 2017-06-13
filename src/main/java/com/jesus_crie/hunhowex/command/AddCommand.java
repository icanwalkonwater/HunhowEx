package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class AddCommand extends Command {

    public AddCommand() {
        super(new CommandInfos("add",
                "Show the invite link to add this bot to your server.",
                "add",
                false,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());

        builder.setTitleWithIcon("Add me to your server !", CommandUtils.ICON_INFO);
        long permissions = Permission.getRaw(
                Permission.MESSAGE_READ,
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_MANAGE,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EMBED_LINKS,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_EXT_EMOJI,

                Permission.KICK_MEMBERS,
                Permission.BAN_MEMBERS,
                Permission.NICKNAME_CHANGE,
                Permission.VIEW_AUDIT_LOGS,
                Permission.NICKNAME_MANAGE,
                Permission.MANAGE_WEBHOOKS,

                Permission.VOICE_SPEAK,
                Permission.VOICE_CONNECT
        );
        builder.addSection("Click [Here](https://discordapp.com/oauth2/authorize?client_id=%id%&scope=bot&permissions=%perm%) !"
            .replace("%id%", HunhowExAPI.getJda().getSelfUser().getId())
            .replace("%perm%", String.valueOf(permissions)));

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
