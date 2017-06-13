package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.HunhowEx;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class StopCommand extends Command {

    public StopCommand() {
        super(new CommandInfos(
                "stop,shutdown",
                "Shutdown the bot. As easy as this.",
                "stop",
                false,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        if (!msg.getAuthor().getId().equalsIgnoreCase("182547138729869314")) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Only the owner of the bot can do that !")).queue();
            return;
        }

        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Shutting down...", CommandUtils.ICON_BED);

        msg.getChannel().sendMessage(builder.build()).complete();
        HunhowEx.getInstance().shutdownTotal();
    }
}
