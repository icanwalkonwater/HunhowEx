package com.jesus_crie.hunhowex.listener;

import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import net.dv8tion.jda.client.events.relationship.FriendRequestReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class FriendRequestListener extends ListenerAdapter {

    @Override
    public void onFriendRequestReceived(FriendRequestReceivedEvent e) {
        Logger.info("[Listener] [Friend] Accepting friend request from " + CommandUtils.getUserString(e.getUser()));
        e.getFriendRequest().accept().queue();
    }
}
