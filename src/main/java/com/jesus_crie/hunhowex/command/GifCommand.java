package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.utils.CommandUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class GifCommand extends Command {

    public GifCommand() {
        super(new CommandInfos("gif,meme",
                "Query some gifs directly from Giphy !",
                "gif [query]",
                false,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) {
        if (args.length > 0) {
            String url = CommandUtils.GIPHY_BASE_GIF + "srach?q=" + String.join("+", args) + "&limit=10&api_key=" + CommandUtils.GIPHY_KEY;

        }
    }
}
