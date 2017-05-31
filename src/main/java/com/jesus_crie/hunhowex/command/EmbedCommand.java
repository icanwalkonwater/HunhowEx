package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.util.Arrays;

public class EmbedCommand extends Command {

    public EmbedCommand() {
        super(new CommandInfos("embed",
                "Easy custom embed (for test)",
                "embed <title> <iconUrl> <description>",
                true,
                "264001800686796800"),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) {
        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon(args[0], args[1]);
        builder.addSection(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
