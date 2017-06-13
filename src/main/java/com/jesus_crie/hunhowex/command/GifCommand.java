package com.jesus_crie.hunhowex.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jesus_crie.hunhowex.exception.BotException;
import com.jesus_crie.hunhowex.exception.ExceptionGravity;
import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class GifCommand extends Command {

    private Random random = new Random();

    public GifCommand() {
        super(new CommandInfos("gif,meme",
                "Query some gifs directly from Giphy !",
                "gif [query]",
                false,
                ""),
            Permission.MESSAGE_WRITE);
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        try {
            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            ObjectMapper mapper = new ObjectMapper();
            String url = CommandUtils.GIPHY_BASE_GIF + "trending?limit=10&api_key=" + CommandUtils.GIPHY_KEY;

            if (args.length > 0) {
                url = CommandUtils.GIPHY_BASE_GIF + "search?q=" + String.join("+", args) + "&limit=10&api_key=" + CommandUtils.GIPHY_KEY;
                builder.setTitleWithIcon("A random gif of \"" + String.join(" ", args) + "\"", CommandUtils.ICON_GIPHY);
            } else
                builder.setTitleWithIcon("Top trending", CommandUtils.ICON_GIPHY);

            JsonNode node = mapper.readValue(new URL(url), JsonNode.class);
            JsonNode gif = node.get("data").get(random.nextInt(node.get("data").size()));

            builder.setImage(gif.get("images").get("original").get("url").asText());

            msg.getChannel().sendMessage(builder.build()).queue();
        } catch (IOException e) {
            new BotException(ExceptionGravity.ERROR, "Error while retrieving datas from Giphy !", e);
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Error while retrieving datas from Giphy !")).queue();
        }
    }
}
