package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.storage.GuildConfig;
import com.jesus_crie.hunhowex.storage.JsonConfig;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class InitCommand extends Command {

    public InitCommand() {
        super(new CommandInfos("init,i",
                "Use this to enable the bot in the current server or reset him. (only the owner of the server can do this)",
                "init,init permissions,init commands,init validate",
                false,
                ""),
            Permission.MESSAGE_WRITE);
        super.registerSubCommands(
                new Permissions(),
                new Commands(),
                new Validate());
    }

    @Override
    public void execute(Message msg, String[] args) {
        if (!msg.getGuild().getOwner().getUser().getId().equalsIgnoreCase(msg.getAuthor().getId())) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Sorry, only the owner of the server can use this !"));
            return;
        }

        if (HunhowExAPI.isGuildRegistered(msg.getGuild().getId())) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(),
                    "Sorry, this guild is already registered. Use `" + CommandUtils.PREFIX + "unregister` to unregister this guild")).queue();
            return;
        }


        if (args.length > 0) {
            Command sub = super.getSubCommand(args[0]);
            if (sub != null)
                sub.execute(msg, super.cutFirstArg(args));
            else
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Unknow subcommand ! Use `" + CommandUtils.PREFIX + "init`")).queue();
            return;
        }

        Member m = msg.getGuild().getMember(HunhowExAPI.getJda().getSelfUser());

        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
        builder.setColor(Color.ORANGE);
        builder.setTitleWithIcon("Initialization", CommandUtils.ICON_TERMINAL);
        builder.addSection("With these commands you can see what permissions are missing.\n");
        builder.addSection("Individual Permission Check", "`" + CommandUtils.PREFIX + "init permissions`");
        builder.addSection("Permissions per command", "`" + CommandUtils.PREFIX + "init commands`");
        builder.addSection("Finalize initialization", "`" + CommandUtils.PREFIX + "init validate`\n");
        builder.addSection("Minimal permissions", "");
        builder.addRegularSection(Permission.MESSAGE_READ.getName(),
                m.hasPermission(Permission.MESSAGE_READ) ? CommandUtils.EMOTE_LITERAL_CHECKMARK : CommandUtils.EMOTE_LITERAL_CROSS,
                true);
        builder.addRegularSection(Permission.MESSAGE_WRITE.getName(),
                m.hasPermission(Permission.MESSAGE_WRITE) ? CommandUtils.EMOTE_LITERAL_CHECKMARK : CommandUtils.EMOTE_LITERAL_CROSS,
                true);
        builder.addRegularSection(Permission.MESSAGE_EMBED_LINKS.getName(),
                m.hasPermission(Permission.MESSAGE_EMBED_LINKS) ? CommandUtils.EMOTE_LITERAL_CHECKMARK : CommandUtils.EMOTE_LITERAL_CROSS,
                true);
        builder.addRegularSection(Permission.MESSAGE_MANAGE.getName(),
                m.hasPermission(Permission.MESSAGE_MANAGE) ? CommandUtils.EMOTE_LITERAL_CHECKMARK : CommandUtils.EMOTE_LITERAL_CROSS,
                true);

        msg.getChannel().sendMessage(builder.build()).queue();
    }

    private class Permissions extends Command {

        public Permissions() {
            super(new CommandInfos("permissions",
                    "Display a full list of all permissions available on Discord.",
                    "init permissions",
                    false,
                    ""
                ), Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) {
            Member m = msg.getGuild().getMember(HunhowExAPI.getJda().getSelfUser());

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Initialization - All Permissions", CommandUtils.ICON_TERMINAL);
            builder.setColor(Color.ORANGE);

            Arrays.asList(Permission.values()).forEach(permission -> {
                if (permission != Permission.UNKNOWN) {
                    if (m.hasPermission(permission))
                        builder.addRegularSection(permission.getName(), CommandUtils.EMOTE_LITERAL_CHECKMARK, true);
                    else
                        builder.addRegularSection(permission.getName(), CommandUtils.EMOTE_LITERAL_CROSS, true);
                }
            });

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }

    private class Commands extends Command {

        public Commands() {
            super(new CommandInfos("commands",
                    "Display a full list of all permissons requiered for each command",
                    "init commands",
                    false,
                    ""),
                Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) {
            Member m = msg.getGuild().getMember(HunhowExAPI.getJda().getSelfUser());

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Initialization - All commands' permissions", CommandUtils.ICON_TERMINAL);
            builder.setColor(Color.ORANGE);

            HunhowExAPI.getCommandsPublic(msg.getGuild().getId()).forEach(command -> {
                List<Permission> ps = command.getRequiredPermissions();
                builder.addRegularSection((m.hasPermission(ps)
                        ? CommandUtils.EMOTE_LITERAL_CHECKMARK
                        : CommandUtils.EMOTE_LITERAL_CROSS) + " " + command.getInfos().getNameCapitalized(),
                    "[" + String.join("] [", ps.stream().map(p -> p.getName()).toArray(String[]::new)) + "]",
                    true);
            });

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }

    private class Validate extends Command {

        public Validate() {
            super(new CommandInfos("validate",
                    "Initialize a configuration for this server",
                    "init validate",
                    false,
                    ""),
                Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) {
            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setColor(Color.ORANGE);
            builder.setTitleWithIcon("Initialization - Validation", CommandUtils.ICON_TERMINAL);

            GuildConfig cfg = new GuildConfig(msg.getGuild().getId(), "default");
            if (HunhowExAPI.registerGuild(cfg)) {
                Logger.info("[Registration] Registered guild " + msg.getGuild().getId() + "/" + msg.getGuild().getName());

                builder.setColor(Color.GREEN);
                builder.addSection("Successfully registered guild " + msg.getGuild().getName() + ".\n");
                builder.addRegularSection("Owner", msg.getGuild().getOwner().getAsMention(), true);
                builder.addRegularSection("Members", "`" + msg.getGuild().getMembers().size() + "`", true);
                builder.addRegularSection("Roles", "`" + msg.getGuild().getRoles().size() + "`", true);
            } else {
                Logger.error("[Registration] Can't create a config for guild " + msg.getGuild().getId() + "/" + msg.getGuild().getName());

                builder.setColor(Color.RED);
                builder.addSection("Failed to create config for this server ! Contact the creator !");
            }

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }
}
