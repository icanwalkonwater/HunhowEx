package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.awt.*;

public class UnregisterCommand extends Command {

    public UnregisterCommand() {
        super(new CommandInfos("unregister",
                "Unregister this server from the config and force the bot to leave the server.",
                "unregister",
                false,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        if (!(msg.getGuild().getOwner().getUser().getId().equalsIgnoreCase(msg.getAuthor().getId())
            || msg.getAuthor().getId().equalsIgnoreCase("182547138729869314"))) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Sorry, only the owner of the server can use this !"));
            return;
        }

        HunhowExAPI.unregisterGuild(msg.getGuild());

        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setColor(Color.ORANGE);
        builder.setTitleWithIcon("Guild " + msg.getGuild().getName() + " successfully unregistered", CommandUtils.ICON_TERMINAL);
        builder.addSection("Leaving this guild...");
        msg.getChannel().sendMessage(builder.build()).complete();

        Logger.info("[Registration] Leaving " + msg.getGuild().getId() + "/" + msg.getGuild().getName());
        msg.getGuild().leave().queue();
    }
}
