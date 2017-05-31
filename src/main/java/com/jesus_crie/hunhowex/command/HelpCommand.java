package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import com.jesus_crie.hunhowex.utils.EmbedPage;
import com.jesus_crie.hunhowex.utils.PaginableMessageBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class HelpCommand extends Command {

    public HelpCommand() {
        super(new CommandInfos("help,h",
                "Display some help about the commands allowed on this server.",
                "help [page],help [command]",
                false,
                ""),
            Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION);
    }

    private LinkedHashMap<String, EmbedPage> createHelp(String guild) {
        LinkedHashMap<String, EmbedPage> pages = new LinkedHashMap<>();
        List<Command> cmds = HunhowExAPI.getCommandsPublic(guild);

        //Index
        String index = "**__Summary__**\nContains help only about the commands allowed on this server.\n\n1. Summary";
        for (int i = 0; i < cmds.size(); i++) {
            Command c = cmds.get(i);
            index += "\n" + (i + 2) + ". `" + CommandUtils.PREFIX + c.getInfos().getName() + "`";
        }
        pages.put(".", new EmbedPage(index));

        //Command page
        for (Command cmd : cmds) {
            StringBuilder data = new StringBuilder();
            data.append("**__" + Character.toUpperCase(cmd.getInfos().getName().charAt(0)) + cmd.getInfos().getName().substring(1) + "__**");
            data.append("\n" + cmd.getInfos().getDescription());
            data.append("\n\n**Aliases:** `" + CommandUtils.PREFIX + String.join("`, `" + CommandUtils.PREFIX, cmd.getInfos().getAlisases()) + "`");
            data.append("\n**Usage:**```http\n" + CommandUtils.PREFIX + String.join("\n" + CommandUtils.PREFIX, cmd.getInfos().getUsage()) + "```");

            pages.put(cmd.getInfos().getName(), new EmbedPage(data.toString()));
        }

        return pages;
    }

    @Override
    public void execute(Message msg, String[] args) {
        LinkedHashMap<String, EmbedPage> pages = createHelp(msg.getGuild().getId());

        PaginableMessageBuilder builder = new PaginableMessageBuilder(msg.getAuthor());
        builder.setTitleWithIcon("Help", CommandUtils.ICON_HELP);
        builder.setThumbnail(CommandUtils.ICON_HELP);
        pages.values().forEach(p -> builder.addPage(p));

        builder.send(msg.getChannel());

        if (args.length > 0) {
            try {
                int page = Integer.parseInt(args[0]);
                builder.setPage(page - 1);
            } catch (NumberFormatException e) {
                int page = new ArrayList<>(pages.keySet()).indexOf(args[0]);
                builder.setPage(page);
            }
        }
    }
}
