package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class ServerInfoCommand extends Command {

    public ServerInfoCommand() {
        super(new CommandInfos("serverinfo,si",
                "Get some infos about this server.",
                "serverinfo",
                false,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) {
        Guild guild = msg.getGuild();

        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Some infos about " + guild.getName(), guild.getIconUrl());
        builder.setThumbnail(guild.getIconUrl());

        builder.addRegularSection("ID", guild.getId(), true);
        builder.addRegularSection("Owner", guild.getOwner().getAsMention(), true);
        builder.addRegularSection("Members", String.valueOf(guild.getMembers().size()), true);
        builder.addRegularSection("Bots",
                String.valueOf(guild.getMembers().stream()
                        .filter(m -> m.getUser().isBot())
                        .toArray().length),
                true);
        builder.addRegularSection("Roles", String.valueOf(guild.getRoles().size()), true);
        builder.addRegularSection("Created on", CommandUtils.stringifyDate(guild.getCreationTime()), true);
        builder.addRegularSection("Bans", String.valueOf(guild.getBans().complete().size()), true);
        builder.addRegularSection("Channels",
                "Text: " + String.valueOf(guild.getTextChannels().size()
                        + "\nVoice: " + String.valueOf(guild.getVoiceChannels().size())),
                true);
        builder.addRegularSection("Emojis",
                String.join(" ", guild.getEmotes().stream()
                        .map(Emote::getAsMention)
                        .toArray(String[]::new)),
                false);

        msg.getChannel().sendMessage(builder.build()).queue();
    }
}
