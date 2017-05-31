package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class BanCommand extends Command {

    public BanCommand() {
        super(new CommandInfos("ban",
                "Ban someone from this sever. Only the owner of this server can do this.",
                "ban <@mention> [<@mention> ...]",
                false,
                ""),
            Permission.MESSAGE_WRITE, Permission.BAN_MEMBERS);
    }

    @Override
    public void execute(Message msg, String[] args) {
        if (!msg.getGuild().getOwner().getUser().getId().equalsIgnoreCase(msg.getAuthor().getId())) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Only the owner of this server can use this !")).queue();
            return;
        }

        if (msg.getMentionedUsers().size() <= 0) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You need to mention at least one user !")).queue();
            return;
        }

        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Ban engine", CommandUtils.ICON_DOOR);
        builder.setImage(CommandUtils.GIF_BANNED);

        msg.getMentionedUsers().forEach(u -> {
            if (!u.getId().equalsIgnoreCase(msg.getGuild().getOwner().getUser().getId()) && msg.getGuild().getSelfMember().canInteract(msg.getGuild().getMember(u))){
                msg.getGuild().getController().ban(msg.getGuild().getMember(u), 0).complete();
            }
        });

        if (msg.getMentionedUsers().size() == 1)
            builder.addSection("**" + CommandUtils.getUserString(msg.getMentionedUsers().get(0)) + "** as been banned from here !");
        else
            builder.addSection(
                    "**" + String.join("**, **", msg.getMentionedUsers().stream().map(u -> CommandUtils.getUserString(u)).toArray(String[]::new))
                            + "** are banned from here !");

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
