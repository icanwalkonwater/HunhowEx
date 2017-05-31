package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class TestCommand extends Command {

    public TestCommand() {
        super(new CommandInfos("test,t",
                "Test command",
                "test",
                true,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) {
        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Just a test", CommandUtils.ICON_INFO);

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
