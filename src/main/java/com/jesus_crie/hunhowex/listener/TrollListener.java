package com.jesus_crie.hunhowex.listener;

import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.logger.Logger;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TrollListener extends ListenerAdapter {

    private Random random = new Random();
    private List<String> taunts = Arrays.asList(
            "Tu veux du poulay ?",
            "Trump ?",
            "J'AI UNE ERECTION !!!",
            "Vous auriez du sel ?",
            "Oh ! T'en a encore mis partout !",
            "PATATE !",
            "T'est moche {author}",
            "{random} Salut toi !",
            "{author} Tu suce ?"
    );

    private List<String> allowedGuild = Arrays.asList(
            "219802541775519744",
            "264001800686796800"
    );

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (e.getAuthor().getId().equalsIgnoreCase(HunhowExAPI.getJda().getSelfUser().getId()))
            return;

        if (!allowedGuild.contains(e.getGuild().getId()))
            return;

        if (e.getChannel().getTopic().contains("{hunhow_random_denied}"))
            return;

        // Salt Flora
        if (e.getAuthor().getId().equalsIgnoreCase("146994395340603392"))
            if (random.nextInt(2) <= 0)
                e.getMessage().addReaction(e.getGuild().getEmotesByName("salt", true).get(0)).queue();

        // Random answers
        if (random.nextInt(150) <= 0) {
            String taunt = taunts.get(random.nextInt(taunts.size()));
            taunt = taunt.replace("{author}", e.getAuthor().getAsMention());

            // Need more memory so we check before doing this
            if (taunt.contains("{random}"))
                taunt = taunt.replace("{random}", getRandomMemberAsMention(e.getGuild()));

            e.getChannel().sendMessage(taunt).queue();
        }
    }

    private String getRandomMemberAsMention(Guild g) {
        return g.getMembers().get(random.nextInt(g.getMembers().size())).getAsMention();
    }
}
