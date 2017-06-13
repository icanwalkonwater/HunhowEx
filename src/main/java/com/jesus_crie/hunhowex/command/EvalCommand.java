package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

import javax.script.*;

public class EvalCommand extends Command {

    public EvalCommand() {
        super(new CommandInfos("eval,e",
                        "Evaluate a java script.",
                        "eval <java code>",
                        false,
                        ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        if (!msg.getAuthor().getId().equalsIgnoreCase("182547138729869314")) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Only the creator can use this command !")).queue();
            return;
        }

        if (args.length <= 0) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You can't execute the void !")).queue();
            return;
        }
        String toExecute = String.join(" ", args);

        // TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO
    }
}
