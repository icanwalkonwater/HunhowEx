package com.jesus_crie.hunhowex.hooks;

import com.jesus_crie.hunhowex.logger.Logger;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ChannelHook<T extends Hookable> implements Hook<TextChannel, T>, Cloneable {

    private Class<T> clazz;
    private TextChannel channel;
    private HashMap<String, T> currentObjects; // <Id, Object>

    public ChannelHook(TextChannel channel, Class<T> c) {
        this.channel = channel;
        this.clazz = c;
        currentObjects = new HashMap<>();
    }

    public String getType() {
        try {
            return (String) clazz.getMethod("getType").invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return "UNKNOWN";
        }
    }

    public void setChannel(TextChannel channel) {
        this.channel = channel;
    }

    public void clearDatas() {
        currentObjects.values().forEach(o -> delete(o));
    }

    public void clearChannel() {
        List<Message> history = channel.getHistory().retrievePast(100).complete();
        while (!history.isEmpty()) {
            channel.deleteMessages(history).complete();
            history = channel.getHistory().retrievePast(100).complete();
        }
    }

    @Override
    public TextChannel getHooked() {
        return channel;
    }

    @Override
    public void send(T o) {
        if (objectExistForId(o))
            edit(o);
        else {
            channel.sendMessage(o.serialize()).queue(m -> {
                o.setMessage(m);
                currentObjects.put(o.getId(), o);
            });
        }
    }

    @Override
    public void delete(T o) {
        T real = currentObjects.get(o.getId());
        if (real != null) {
            real.getMessage().delete().queue();
            currentObjects.remove(real.getId());
        }
    }

    @Override
    public void edit(T o) {
        if (!objectExistForId(o))
            send(o);
        else {
            Message m = retrieveById(o);
            m.editMessage(o.serialize()).queue(mn -> {
                o.setMessage(mn);
                currentObjects.put(o.getId(), o);
            });
        }
    }

    @Override
    public boolean objectExistForId(T o) {
        return currentObjects.keySet().contains(o.getId());
    }

    @Override
    public List<String> getCurrentMessagesId() {
        return new ArrayList<>(currentObjects.keySet());
    }

    @Override
    public List<Message> retrieveMessages() {
        return currentObjects.values()
                .stream()
                .map(Hookable::getMessage)
                .collect(Collectors.toList());
    }

    @Override
    public Message retrieveById(T t) {
        return currentObjects.get(t.getId()).getMessage();
    }

    @Override
    public Message retrieveByMessageId(String id) {
        return retrieveMessages()
                .stream()
                .filter(m -> m.getId().equals(id))
                .findFirst().get();
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            Logger.error("Can't happened");
        }
        return this;
    }
}
