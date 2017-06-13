package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordReactCommand extends Command {

    private static HashMap<Character, String> emoteMain;
    private static HashMap<Character, String> emoteSecondary;

    public WordReactCommand() {
        super(new CommandInfos("wordreact,wr,react",
                "Print a word in emojis on the last message.",
                "wordreact <word>",
                false,
                ""),
            Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_HISTORY);
        emoteMain = new HashMap<>();
        emoteSecondary = new HashMap<>();

        emoteMain.put('a', "\uD83C\uDDE6");
        emoteMain.put('b', "\uD83C\uDDE7");
        emoteMain.put('c', "\uD83C\uDDE8");
        emoteMain.put('d', "\uD83C\uDDE9");
        emoteMain.put('e', "\uD83C\uDDEA");
        emoteMain.put('f', "\uD83C\uDDEB");
        emoteMain.put('g', "\uD83C\uDDEC");
        emoteMain.put('h', "\uD83C\uDDED");
        emoteMain.put('i', "\uD83C\uDDEE");
        emoteMain.put('j', "\uD83C\uDDEF");
        emoteMain.put('k', "\uD83C\uDDF0");
        emoteMain.put('l', "\uD83C\uDDF1");
        emoteMain.put('m', "\uD83C\uDDF2");
        emoteMain.put('n', "\uD83C\uDDF3");
        emoteMain.put('o', "\uD83C\uDDF4");
        emoteMain.put('p', "\uD83C\uDDF5");
        emoteMain.put('q', "\uD83C\uDDF6");
        emoteMain.put('r', "\uD83C\uDDF7");
        emoteMain.put('s', "\uD83C\uDDF8");
        emoteMain.put('t', "\uD83C\uDDF9");
        emoteMain.put('u', "\uD83C\uDDFA");
        emoteMain.put('v', "\uD83C\uDDFB");
        emoteMain.put('w', "\uD83C\uDDFC");
        emoteMain.put('x', "\uD83C\uDDFD");
        emoteMain.put('y', "\uD83C\uDDFE");
        emoteMain.put('z', "\uD83C\uDDFF");

        emoteSecondary.put('a', "\uD83C\uDD70");
        emoteSecondary.put('o', "\uD83C\uDD7E");
        emoteSecondary.put('p', "\uD83C\uDD7F");
        emoteSecondary.put('m', "\u24C2");
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        if (args.length <= 0) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You need to gimme a word !")).queue();
            return;
        }

        Message toReact = msg.getChannel().getHistory().retrievePast(2).complete().get(0);
        String raw = String.join("", args).toLowerCase();
        List<Character> used = new ArrayList<>();

        for (char c : raw.toCharArray()) {
            if (!used.contains(c)) {
                toReact.addReaction(emoteMain.get(c)).queue();
                used.add(c);
            } else if (emoteSecondary.keySet().contains(c)) {
                toReact.addReaction(emoteSecondary.get(c)).queue();
            }
        }
    }
}
