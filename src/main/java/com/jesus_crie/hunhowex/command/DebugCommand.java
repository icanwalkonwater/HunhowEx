package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.awt.*;

public class DebugCommand extends Command {

    public DebugCommand() {
        super(new CommandInfos("debug,d",
                "Enable or disable the debug mode",
                "debug [on],debug [off]",
                true,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        boolean enable;
        if (args.length <= 0)
            enable = !HunhowExAPI.isDebugEnable();
        else if (args[0].equalsIgnoreCase("on"))
            enable = true;
        else if (args[0].equalsIgnoreCase("off"))
            enable = false;
        else
            enable = !HunhowExAPI.isDebugEnable();

        HunhowExAPI.enableDebug(enable);

        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Debug mode: " + (enable ? "ENABLE" : "DISABLE"), CommandUtils.ICON_TERMINAL);
        builder.setColor(Color.ORANGE);

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
