package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.storage.GuildConfig;
import com.jesus_crie.hunhowex.storage.JsonConfig;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super(new CommandInfos("config,cfg",
                "Show and modify the current configuration if this server. Only usable by the owner.",
                "config,config set <variable> <value>,config reset <variable>",
                false,
                CommandUtils.GUILD_TEST_ZONE),
            Permission.MESSAGE_WRITE);
        super.registerSubCommands(
                new Set(),
                new Reset()
        );
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        if (!(msg.getAuthor().getId().equals(msg.getGuild().getOwner().getUser().getId())
            || msg.getAuthor().getId().equals("182547138729869314"))) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Only the owner can use this command !")).queue();
            return;
        }

        if (args.length > 0) {
            Command c = super.getSubCommand(args[0]);
            if (c != null)
                c.execute(msg, super.cutFirstArg(args));
            else
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Unknown subcommand !")).queue();
            return;
        }

        GuildConfig config = HunhowExAPI.getGuildPreferences(msg.getGuild().getId());

        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Configuration", CommandUtils.ICON_TERMINAL);
        builder.setDescription("To change a value use `" + CommandUtils.PREFIX + "config set <variable> <value>`.\n" +
                "To reset a value use `" + CommandUtils.PREFIX + "config reset <variable>`.\n" +
                "The variable name is in the brackets below.");

        builder.addField("Auto Playlist (auto)",
                "Provide a youtube/soundcloud playlist here. You can provide only the id."
                    + getConfigString(config.getMusicAutoPlaylist()), false);

        builder.addField("Welcome Message (welcome_msg)",
                "You can use {user} to mention the new guy."
                    + getConfigString(config.getWelcomeMessage()),
                false);

        builder.addField("Welcome Channel (welcome_chan)",
                "You can use {main} to use the general channel."
                    + getConfigString(config.getWelcomeChannel()),
                false);

        msg.getChannel().sendMessage(builder.build()).queue();
    }

    private String getConfigString(String raw) {
        return "```http\n" + raw + "```";
    }

    private class Set extends Command {

        public Set() {
            super(new CommandInfos("set",
                    "Set a config variable.",
                    "config set <variable> <value>",
                    false,
                    ""),
                Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {
            if (args.length < 2) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "I need more arguments !")).queue();
                return;
            }

            GuildConfig cfg = HunhowExAPI.getGuildPreferences(msg.getGuild().getId());
            String value = String.join(" ", super.cutFirstArg(args));
            boolean error = false;

            switch (args[0].toLowerCase()) {
                case "auto":
                    cfg.setMusicAutoPlaylist(value);
                    HunhowExAPI.getMusicManager().updateGuild(msg.getGuild());
                    break;
                case "welcome_msg":
                    cfg.setWelcomeMessage(value);
                    break;
                case "welcome_chan":
                    cfg.setWelcomeChannel(value);
                    break;
                default:
                    error = true;
                    break;
            }

            if (error) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Unknown variable !")).queue();
                return;
            } else {
                EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                builder.setTitleWithIcon("Configutation changed !", CommandUtils.ICON_CHECK);
                builder.addSection("Config updated, changes are immediately available.");

                msg.getChannel().sendMessage(builder.build()).queue();
            }
        }
    }

    private class Reset extends Command {

        public Reset() {
            super(new CommandInfos("reset",
                            "Reset a config variable.",
                            "config reset <variable> <value>",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {
            if (args.length < 0) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "I need more arguments !")).queue();
                return;
            }

            GuildConfig cfg = HunhowExAPI.getGuildPreferences(msg.getGuild().getId());

            switch (args[0].toLowerCase()) {
                case "auto":
                    cfg.setMusicAutoPlaylist(GuildConfig.getDefault().getMusicAutoPlaylist());
                    HunhowExAPI.getMusicManager().updateGuild(msg.getGuild());
                    break;
                case "welcome_msg":
                    cfg.setWelcomeMessage(GuildConfig.getDefault().getWelcomeMessage());
                    break;
                case "welcome_chan":
                    cfg.setWelcomeChannel(GuildConfig.getDefault().getWelcomeChannel());
                    break;
                default:
                    break;
            }

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Configutation changed !", CommandUtils.ICON_CHECK);
            builder.addSection("Config updated, changes are immediately available.");

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }
}
