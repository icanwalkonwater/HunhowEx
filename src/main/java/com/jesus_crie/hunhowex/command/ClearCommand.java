package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class ClearCommand extends Command {

    public ClearCommand() {
        super(new CommandInfos("clear,c",
                "Clear a certain amount of messages.",
                "clear <amount>",
                false,
                ""),
            Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE, Permission.MESSAGE_HISTORY);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        if (!msg.getAuthor().getId().equals(msg.getGuild().getOwner().getUser().getId())) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Only the owner can use this command !")).queue();
        }

        if (args.length <= 0) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You must provide a number !")).queue();
            return;
        }

        try {
            int amount = Integer.parseInt(args[0]);
            if (amount < 1 || amount > 100) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You can't delete more than 100 messages or less than 1 !")).queue();
                return;
            }

            if (amount > 1)
                msg.getChannel().getHistory().retrievePast(amount).queue(history ->
                    ((TextChannel) msg.getChannel()).deleteMessages(history).queue());
            else
                msg.getChannel().getHistory().retrievePast(amount).queue(history -> history.forEach(m -> m.delete().queue()));

        } catch (NumberFormatException e) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "This is not a number !")).queue();
            return;
        }
    }
}
