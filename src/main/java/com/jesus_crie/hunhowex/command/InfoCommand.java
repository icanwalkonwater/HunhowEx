package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class InfoCommand extends Command {

    public InfoCommand() {
        super(new CommandInfos("infos,info",
                "Display some infos about me.",
                "infos",
                false,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Infos", msg.getJDA().getSelfUser().getEffectiveAvatarUrl());
        builder.setThumbnail(msg.getJDA().getSelfUser().getEffectiveAvatarUrl());

        String mainRole = msg.getGuild().getSelfMember().getRoles().size() <= 0
                ? "None" : msg.getGuild().getSelfMember().getRoles().get(0).getName();

        builder.addField("Self infos",
                "User: `" + msg.getJDA().getSelfUser().getId() + "/" + CommandUtils.getUserString(msg.getJDA().getSelfUser())
                + "`\nMain Role: `" + mainRole
                + "`\nGuild: `" + msg.getJDA().getGuilds().size()
                + "`\nUptime: **" + HunhowExAPI.getUptime() + "**",
                false);
        builder.addField("Developement",
                "Main API: **[JDA](https://github.com/DV8FromTheWorld/JDA) v3.1.1_210 by DV8FromTheWorld**"
                + "\nAudio API: **[LavaPlayer](https://github.com/sedmelluq/lavaplayer) v1.2.39 by Sedmelluq**"
                + "\nSources: **[Github](https://github.com/JesusCrie/HunhowEx)**",
                false);

        if (msg.getGuild().getMemberById("182547138729869314") != null)
            builder.addField("Creator",
                "Made by " + msg.getGuild().getMemberById("182547138729869314").getAsMention()
                        + "\nHe is awesome ! :heart:",
                        false);
        else
            builder.addField("Creator", "Made by JesusCrie#6701\nHe is awesome you need to invite him here ! :heart:", false);

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
