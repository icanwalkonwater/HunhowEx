package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.logger.Logger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {

    private CommandInfos infos;
    private List<Command> subCommands;
    private List<Permission> perms;

    public Command(CommandInfos infos, Permission... perms) {
        this.infos = infos;
        subCommands = new ArrayList<>();
        this.perms = new ArrayList<>();
        this.perms.addAll(Arrays.asList(perms));

    }

    public List<Permission> getRequiredPermissions() {
        return perms;
    }

    public CommandInfos getInfos() {
        return infos;
    }

    public boolean hasSubCommands() {
        return subCommands.isEmpty();
    }

    public abstract void execute(Message msg, String[] args) throws PermissionException;

    protected boolean hasPermission(Message msg) {
        return true;
    }

    public void registerSubCommands(Command... subs) {
        for (Command c : subs)
            subCommands.add(c);
    }

    public Command getSubCommand(String name) {
        for (Command c : subCommands)
            if (c.getInfos().getName().equalsIgnoreCase(name))
                return c;
        return null;
    }

    public String[] cutFirstArg(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
