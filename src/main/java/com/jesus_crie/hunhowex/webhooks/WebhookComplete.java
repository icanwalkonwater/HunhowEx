package com.jesus_crie.hunhowex.webhooks;

import com.jesus_crie.hunhowex.exception.BotException;
import com.jesus_crie.hunhowex.exception.ExceptionGravity;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.impl.WebhookImpl;
import net.dv8tion.jda.core.requests.Request;
import net.dv8tion.jda.core.requests.Response;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.Route;
import org.apache.http.util.Args;
import org.json.JSONObject;

@Deprecated
public class WebhookComplete extends WebhookImpl {

    public WebhookComplete(TextChannel channel, long id) {
        super(channel, id);
    }

    public WebhookComplete(WebhookImpl s) {
        super(s.getChannel(), s.getIdLong());
        super.setOwner(s.getOwner());
        super.setToken(s.getToken());
        super.setUser(s.getDefaultUser());
    }

    public void sendMessage(String username, String avatar_url, MessageEmbed... embeds) {
        sendMessage(new WebhookMessageBuilder().setUsername(username).setAvatarUrl(avatar_url).addEmbed(embeds).build());
    }

    public void sendMessage(String username, String avatar_url, String msg) {
        sendMessage(new WebhookMessageBuilder().setUsername(username).setAvatarUrl(avatar_url).append(msg).build());
    }

    public void sendMessage(WebhookMessage msg) {
        Args.notNull(msg, "Message");

        if (!msg.getEmbeds().isEmpty()) {
            AccountType type = getJDA().getAccountType();
            MessageEmbed embed = msg.getEmbeds().get(0);
            Args.check(embed.isSendable(type),
                    "Provided Message contains an embed with a length greater than %d characters, which is the max for %s accounts!",
                    type == AccountType.BOT ? MessageEmbed.EMBED_MAX_LENGTH_BOT : MessageEmbed.EMBED_MAX_LENGTH_CLIENT, type);
        }

        Route.CompiledRoute route = Route.Webhooks.EXECUTE_WEBHOOK.compile(getId(), getToken());
        JSONObject json = msg.toJSONObject();
        new RestAction<Void>(getJDA(), route, json) {
            @Override
            protected void handleResponse(Response response, Request<Void> request) {
                if (response.isError())
                    new BotException(ExceptionGravity.ERROR, "Error while executing webhook.", response.exception);
            }
        }.queue();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
    }
}
