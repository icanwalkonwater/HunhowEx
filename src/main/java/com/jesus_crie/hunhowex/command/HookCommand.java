package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.hooks.ChannelHook;
import com.jesus_crie.hunhowex.hooks.DevTest;
import com.jesus_crie.hunhowex.hooks.HookManager;
import com.jesus_crie.hunhowex.hooks.Hookable;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class HookCommand extends Command {

    public HookCommand() {
        super(new CommandInfos("hook,h",
                "Create and use an hook for the current channel.",
                "hook link,hook create <message>,hook edit <msg id> <message>,hook delete <msg id>",
                true,
                CommandUtils.GUILD_TEST_ZONE),
            Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE);

        /*super.registerSubCommands(
                new Link(),
                new Create(),
                new Edit(),
                new Delete()
        );*/

        //HunhowExAPI.registerHook(new ChannelHook<Test>(HunhowExAPI.getJda().getTextChannelById("264001800686796800")));
    }


    @Override
    @SuppressWarnings("unchecked")
    public void execute(Message msg, String[] args) throws PermissionException {
        ChannelHook<DevTest> hook = HunhowExAPI.getHookManager().getHookForChannel(msg.getGuild().getTextChannelById("323863460116955137"));
        hook.send(new DevTest(args[0], args[1]));

        /*if (args.length <= 0) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Nope")).queue();
            return;
        }

        Command sub = super.getSubCommand(args[0]);
        if (sub != null)
            sub.execute(msg, super.cutFirstArg(args));
        else
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Unknow subcommand")).queue();
         */
    }
}
