package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class UserInfoCommand extends Command {

    public UserInfoCommand() {
        super(new CommandInfos("userinfo,ui",
                "Get some informations about someone on this server or about yourself.",
                "userinfo [@mention]",
                false,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) {
        final Member m;
        if (msg.getMentionedUsers().size() <= 0)
            m = msg.getGuild().getMember(msg.getAuthor());
        else
            m = msg.getGuild().getMember(msg.getMentionedUsers().get(0));

        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setColor(m.getColor());
        builder.setThumbnail(m.getUser().getEffectiveAvatarUrl());
        builder.setTitleWithIcon("Some infos about \"" + m.getEffectiveName() + "\"", m.getUser().getEffectiveAvatarUrl());

        builder.addRegularSection("User ID", m.getUser().getId(), true);
        builder.addRegularSection("Full name", CommandUtils.getUserString(m.getUser()), true);
        builder.addRegularSection("Status", m.getOnlineStatus().getKey().toUpperCase(), true);
        builder.addRegularSection("Current Game", m.getGame().getName(), true);
        builder.addRegularSection("Join At", CommandUtils.stringifyDate(m.getJoinDate()), true);

        List<String> roles = new ArrayList<>();
        String colorRole = m.getRoles().stream().filter(r ->
                (r.getColor() != null) && (m.getColor() != null) && (r.getColor().equals(m.getColor())))
            .findFirst().get().getName();

        if (m.getRoles().isEmpty())
            roles.add("None");
        else
            m.getRoles().stream().map(r -> r.getName()).forEachOrdered(r -> roles.add(r));
        roles.set(roles.indexOf(colorRole), "**__" + colorRole + "__**");

        builder.addRegularSection("Roles",
                String.join(", ", roles), false);

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
