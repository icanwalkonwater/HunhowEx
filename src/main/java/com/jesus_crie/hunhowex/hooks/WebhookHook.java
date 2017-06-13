package com.jesus_crie.hunhowex.hooks;

import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.webhooks.WebhookComplete;
import com.jesus_crie.hunhowex.webhooks.WebhookMessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Webhook;
import net.dv8tion.jda.core.entities.impl.WebhookImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Deprecated
public class WebhookHook<T extends Hookable> implements Hook<WebhookComplete, T> {

    private int size;
    private String username;
    private String avatarUrl;

    private WebhookComplete webhook;
    private HashMap<String, Message> messages;
    private HashMap<String, T> currentObjects;

    public WebhookHook(Webhook webhook, int embedCount, String username, String avatarUrl) {
        this.webhook = new WebhookComplete((WebhookImpl) webhook);
        size = embedCount;
        this.username = username;
        this.avatarUrl = avatarUrl;
        messages = new HashMap<>();
        currentObjects = new HashMap<>();
    }

    @Override
    public WebhookComplete getHooked() {
        return webhook;
    }

    @Override
    public void send(T o) {
        if (currentObjects.get(o.getId()) != null) {
            Logger.info("edit obj");
            edit(o);
        } else {
            Logger.info("create obj");
            boolean full = true;
            Message m = null;

            if (!messages.isEmpty()) {
                Logger.info("msg not empty");
                m = (Message) messages.values().toArray()[messages.values().size()];
                full = (m.getEmbeds().size() >= size);
            }
            WebhookMessageBuilder builder;
            if (!full)
                builder = new WebhookMessageBuilder(m);
            else {
                Logger.info("Create new message");
                builder = new WebhookMessageBuilder();
                builder.setUsername(username);
                builder.setAvatarUrl(avatarUrl);
            }
            builder.addEmbed(o.serialize());

            if (!full)
                o.setMessage(m.editMessage(builder.build()).complete());
            else {
                Logger.info("sending message");
                webhook.sendMessage(builder.build());
                Message mlast = webhook.getChannel().getHistory().retrievePast(10).complete()
                        .stream()
                        .filter(mh -> mh.getAuthor().getName().equalsIgnoreCase(username) && mh.isWebhookMessage())
                        .findFirst()
                        .get();

                o.setMessage(mlast);
            }

            Logger.info(o.getMessage().getId() + " || " + o.getId());

            Logger.info("adding obj to map currents");
            currentObjects.put(o.getId(), o);
        }
    }

    @Override
    public void delete(T o) {
        if (currentObjects.get(o.getId()) == null)
            return;
        T real = currentObjects.get(o.getId());
        WebhookMessageBuilder builder = new WebhookMessageBuilder(real.getMessage());
        builder.clearEmbeds();
        builder.addEmbed(real.getMessage().getEmbeds()
            .stream()
            .filter(e -> !e.getFooter().getText().equalsIgnoreCase(real.getId()))
            .toArray(MessageEmbed[]::new));

        real.getMessage().editMessage(builder.build()).queue();
        currentObjects.remove(o.getId());
    }

    @Override
    public void edit(T o) {
        if (currentObjects.get(o.getId()) == null) {
            send(o);
            return;
        }
        T real = currentObjects.get(o.getId());
        WebhookMessageBuilder builder = new WebhookMessageBuilder(real.getMessage());
        builder.clearEmbeds();
        real.getMessage().getEmbeds().forEach(e -> {
            if (e.getFooter().getText().equalsIgnoreCase(real.getId())) {
                e = o.serialize();
                currentObjects.replace(real.getId(), o);
            }
            builder.addEmbed(e);
        });

        real.getMessage().editMessage(builder.build()).queue();
    }

    @Override
    public boolean objectExistForId(T o) {
        return currentObjects.get(o.getId()) == null;
    }

    @Override
    public List<String> getCurrentMessagesId() {
        return new ArrayList<>(messages.keySet());
    }

    @Override
    public List<Message> retrieveMessages() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public Message retrieveById(T o) {
        if (currentObjects.get(o) != null)
            return currentObjects.get(o).getMessage();
        return null;
    }

    @Override
    public Message retrieveByMessageId(String id) {
        return messages.get(id);
    }
}
