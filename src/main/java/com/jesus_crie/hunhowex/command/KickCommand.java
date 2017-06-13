package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class KickCommand extends Command {

    public KickCommand() {
        super(new CommandInfos("kick",
                "Kick someone from this server. Only the owner of the server can use this.",
                "kick <@mention> [<@mention> ...]",
                false,
                ""),
            Permission.MESSAGE_WRITE, Permission.KICK_MEMBERS);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        if (!msg.getGuild().getOwner().getUser().getId().equalsIgnoreCase(msg.getAuthor().getId())) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Only the owner of this server can use this !")).queue();
            return;
        }

        if (msg.getMentionedUsers().size() <= 0) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You need to mention at least one user !")).queue();
            return;
        }

        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Kick machine", CommandUtils.ICON_DOOR);

        msg.getMentionedUsers().forEach(u -> {
            if (!u.getId().equalsIgnoreCase(msg.getGuild().getOwner().getUser().getId()) && msg.getGuild().getSelfMember().canInteract(msg.getGuild().getMember(u))){
                msg.getGuild().getController().kick(msg.getGuild().getMember(u)).complete();
            }
        });

        if (msg.getMentionedUsers().size() == 1)
            builder.addSection("**" + CommandUtils.getUserString(msg.getMentionedUsers().get(0)) + "** as been kicked from here !");
        else
            builder.addSection(
                    "**" + String.join("**, **", msg.getMentionedUsers().stream().map(u -> CommandUtils.getUserString(u)).toArray(String[]::new))
                    + "** have been kicked out !");

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
