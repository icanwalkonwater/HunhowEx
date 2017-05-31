package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.time.Duration;

public class PingCommand extends Command {

    public PingCommand() {
        super(new CommandInfos("ping",
                "You seriously don't know what it is ?",
                "ping",
                false,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) {
        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Calculating ping...", CommandUtils.ICON_INFO);
        builder.addSection("This time include message building time.");

        msg.getChannel().sendMessage(builder.build()).queue(m -> {
            long pingSent = Duration.between(msg.getCreationTime(), m.getCreationTime()).toMillis();
            builder.setTitleWithIcon("Ping: " + pingSent + "ms", CommandUtils.ICON_INFO);

            m.editMessage(builder.build()).queue();
        });
    }
}
