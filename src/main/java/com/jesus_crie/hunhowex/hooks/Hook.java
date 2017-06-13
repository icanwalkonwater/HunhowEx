package com.jesus_crie.hunhowex.hooks;

import net.dv8tion.jda.core.entities.Message;

import java.util.List;

public interface Hook<T, V extends Hookable> {

    T getHooked();

    void send(V o);

    void delete(V o);

    void edit(V o);

    boolean objectExistForId(V o);

    List<String> getCurrentMessagesId();

    List<Message> retrieveMessages();

    Message retrieveById(V v);

    Message retrieveByMessageId(String id);
}
