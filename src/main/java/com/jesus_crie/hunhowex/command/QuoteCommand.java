package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.List;

public class QuoteCommand extends Command {

    public QuoteCommand() {
        super(new CommandInfos("quote,q",
                "Quote a message that was send earlier.",
                "quote <message>,quote <message_id>",
                false,
                ""),
            Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        if (args.length <= 0) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You can't quote the void !")).queue();
            return;
        }

        String id = null;

        if (CommandUtils.isId(args[0]))
            id = args[0];
        else {
            String query = String.join(" ", args);

            List<Message> history = msg.getChannel().getHistory().retrievePast(100).complete();
            try {
                id = history.stream().filter(m -> m.getRawContent().contains(query)).findFirst().get().getId();
            } catch (NullPointerException e) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "No message found or too old !")).queue();
            }
        }

        Message toQuote = msg.getChannel().getMessageById(id).complete();
        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Sent on " + CommandUtils.stringifyDate(toQuote.getCreationTime())
                + " by " + toQuote.getAuthor().getName(), toQuote.getAuthor().getEffectiveAvatarUrl());
        builder.addSection(toQuote.getRawContent());

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
