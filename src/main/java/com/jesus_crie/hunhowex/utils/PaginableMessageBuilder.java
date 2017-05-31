package com.jesus_crie.hunhowex.utils;

import com.jesus_crie.hunhowex.HunhowEx;
import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.listener.ReactionListener;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;

public class PaginableMessageBuilder extends ReactableMessageBuilder {

    private User user;
    private List<EmbedPage> pages;
    private Message message;
    private int currentPage = 0;
    private String title;
    private String iconUrl;

    public PaginableMessageBuilder(User u) {
        super(u);
        super.setReactions(CommandUtils.EMOTE_PREVIOUS, CommandUtils.EMOTE_NEXT);
        pages = new ArrayList<>();
        this.user = u;
    }

    @Override
    public void setTitleWithIcon(String title, String iconUrl) {
        this.title = title;
        this.iconUrl = iconUrl;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public void addPage(EmbedPage page) {
        pages.add(page);
    }

    public void addPages(List<EmbedPage> pages) {
        this.pages.addAll(pages);
    }

    public void setPage(int index) {
        if (message != null) {
            if (index < 0)
                index = 0;
            else if (index >= pages.size())
                index = pages.size() - 1;

            currentPage = index;
            message.editMessage(build()).complete();
        }
    }

    @Override
    public MessageEmbed build() {
        if (iconUrl == null)
            super.setTitle(title + " (" + (currentPage + 1) + "/" + pages.size() + ")");
        else
            super.setTitleWithIcon(title + " (" + (currentPage + 1) + "/" + pages.size() + ")", iconUrl);

        super.clearSections();
        super.content.append(pages.get(currentPage).toString());
        return super.build();
    }

    public void cleanup() {
        if (!HunhowEx.isConnected())
            return;

        message.clearReactions().queue();
        HunhowExAPI.unregisterListener(super.listener);
    }

    @Override
    public void send(MessageChannel channel) {
        message = channel.sendMessage(build()).complete();

        for (String s : super.emotes)
            message.addReaction(s).complete();

        super.listener = new ReactionListener(message.getId(), e -> {
            if (e.getUser().getId().equalsIgnoreCase(user.getId())) {

                if (e.getReaction().getEmote().getName().equalsIgnoreCase(CommandUtils.EMOTE_PREVIOUS))
                    setPage(currentPage - 1);
                else if (e.getReaction().getEmote().getName().equalsIgnoreCase(CommandUtils.EMOTE_NEXT))
                    setPage(currentPage + 1);
            }

            e.getReaction().removeReaction(e.getUser()).queue();
        });
        HunhowExAPI.registerListener(super.listener);

        //Auto destroy 1 minute
        ThreadManager.startTimer(() -> cleanup(), 60000);
    }
}
