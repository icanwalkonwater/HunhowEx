package com.jesus_crie.hunhowex.listener;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.command.Command;
import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.DebugMessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getRawContent().startsWith(CommandUtils.PREFIX)) {
            String[] fullCmd = e.getMessage().getRawContent().substring(1).split(" ");
            Command cmd = HunhowExAPI.getCommand(fullCmd[0]);

            if (cmd != null) {
                if (e.isFromType(ChannelType.PRIVATE) || e.isFromType(ChannelType.GROUP) || e.isFromType(ChannelType.UNKNOWN)) {
                    e.getChannel().sendMessage(CommandUtils.getMessagePrivateChannel(e.getAuthor())).queue();
                    return;
                }

                if (!HunhowExAPI.isGuildRegistered(e.getGuild().getId()) && !cmd.getInfos().getName().equalsIgnoreCase("init")) {
                    e.getChannel().sendMessage(CommandUtils.getMessageGuildNotRegistered(e.getAuthor())).queue();
                    return;
                }

                if (cmd.getInfos().isServerOnly() && !cmd.getInfos().getServerOnly().contains(e.getGuild().getId()))
                    return;

                Logger.info("[Listener] [Command] From server \"" +
                        e.getGuild().getName() + "\", by " +
                        CommandUtils.getUserString(e.getMessage().getAuthor()) + " issued \"" +
                        e.getMessage().getRawContent() + "\"");
                e.getMessage().delete().queue();
                e.getChannel().sendTyping().complete();
                if (HunhowExAPI.isDebugEnable())
                    e.getChannel().sendMessage(new DebugMessageBuilder(e.getMessage()).build()).complete();
                new Thread(() -> {
                    try {
                        cmd.execute(e.getMessage(), Arrays.copyOfRange(fullCmd, 1, fullCmd.length));
                    } catch (PermissionException e1) {
                        e.getChannel().sendMessage(CommandUtils.getMessageError(e.getAuthor(), "Missing permission: " + e1.getPermission().getName())).queue();
                        Logger.error("[Permissions] Missing permission: " + e1.getPermission().getName());
                    }
                }).start();
            }
        }
    }
}
