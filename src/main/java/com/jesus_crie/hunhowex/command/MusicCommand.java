package com.jesus_crie.hunhowex.command;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jesus_crie.hunhowex.HunhowExAPI;
import com.jesus_crie.hunhowex.exception.BotException;
import com.jesus_crie.hunhowex.exception.ExceptionGravity;
import com.jesus_crie.hunhowex.music.GuildMusicManager;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

public class MusicCommand extends Command {

    public MusicCommand() {
        super(new CommandInfos("music,m",
                "Use this to use the music component. You can load, skip, stop tracks, change the volume, ect...",
                "music summon," +
                        "music play <url|identifier>," +
                        "music skip," +
                        "music stop," +
                        "music repeat," +
                        "music volume [volume]," +
                        "music pause," +
                        "music queue," +
                        "music current," +
                        "music shuffle",
                false,
                ""),
            Permission.MESSAGE_WRITE, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);

        super.registerSubCommands(
                new Summon(),
                new Play(),
                new Skip(),
                new Stop(),
                new Repeat(),
                new Volume(),
                new Pause(),
                new Queue(),
                new Current(),
                new Shuffle()
        );
    }

    private static boolean isUserSameChannel(Message msg) {
        GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());
        VoiceChannel toCheck = manager.getUserVoiceState(msg.getAuthor()).getChannel();

        if (toCheck == null)
            return false;
        return toCheck.getId().equalsIgnoreCase(msg.getGuild().getAudioManager().getConnectedChannel().getId());
    }

    @Override
    public void execute(Message msg, String[] args) {
        if (args.length <= 0) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Unknown subcommand, use `" + CommandUtils.PREFIX + "help music` for help")).queue();
            return;
        }
        Command sub = super.getSubCommand(args[0]);

        if (sub == null) {
            msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Unknown subcommand, use `" + CommandUtils.PREFIX + "help music` for help")).queue();
            return;
        }

        sub.execute(msg, super.cutFirstArg(args));
    }

    private class Summon extends Command {

        public Summon() {
            super(new CommandInfos("summon",
                    "Summon the bot in your channel.",
                    "music summon",
                    false,
                    ""),
                Permission.MESSAGE_WRITE, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
        }

        @Override
        public void execute(Message msg, String[] args) {
            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());
            GuildVoiceState state = manager.getUserVoiceState(msg.getAuthor());

            if (!state.inVoiceChannel()) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You're not in a voice channel !")).queue();
                return;
            }

            if (manager.isConnected()) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Already connected to a voice channel !")).queue();
                return;
            }

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Connecting to " + state.getChannel().getName(), CommandUtils.ICON_MUSIC);

            manager.connectToChannel(state.getChannel());
            manager.skipTrack();

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }

    private class Play extends Command {

        public Play() {
            super(new CommandInfos("play",
                    "Queue a song.",
                    "music play <url|identifier>",
                    false,
                    ""),
                Permission.MESSAGE_WRITE, Permission.VOICE_SPEAK);
        }

        @Override
        public void execute(Message msg, String[] args) {
            if (args.length <= 0) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Usage: `" + CommandUtils.PREFIX + "music play <url|identifier>`")).queue();
                return;
            }

            if (!isUserSameChannel(msg)) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You must be in the same channel as me to use this command !")).queue();
                return;
            }

            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());
            HunhowExAPI.getMusicManager().loadAudioTrack(args[0],
                    track -> {
                        manager.playTrack(track);

                        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                        builder.setTitleWithIcon("Track successfully loaded !", CommandUtils.ICON_MUSIC);
                        builder.addSection("Loaded track **" + track.getInfo().title + "**");

                        msg.getChannel().sendMessage(builder.build()).queue();
                    }, playlist -> {
                        playlist.getTracks().forEach(t -> manager.playTrack(t));

                        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                        builder.setTitleWithIcon("Playlist successfully loaded !", CommandUtils.ICON_MUSIC);
                        builder.addSection("Loaded playlist **" + playlist.getName() + "**");

                        msg.getChannel().sendMessage(builder.build()).queue();
                    }, () -> {
                        EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                        builder.setTitleWithIcon("No matches for `" + args[0] + "` =(", CommandUtils.ICON_MUSIC);

                        msg.getChannel().sendMessage(builder.build()).queue();
                    }, () ->
                        msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "Error while loading `" + args[0] + "`")).queue()
                    );
        }
    }

    private class Skip extends Command {

        public Skip() {
            super(new CommandInfos("skip",
                    "Skip the current track.",
                    "music skip",
                    false,
                    ""),
                Permission.MESSAGE_WRITE, Permission.VOICE_SPEAK);
        }

        @Override
        public void execute(Message msg, String[] args) {
            if (!isUserSameChannel(msg)) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You must be in the same channel as me to use this command !")).queue();
                return;
            }

            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());

            String old = manager.getCurrentTrack().getInfo().title;
            manager.skipTrack();

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Skipping track", CommandUtils.ICON_MUSIC);
            builder.addRegularSection("Skipped", old, false);
            builder.addRegularSection("Playing", manager.getCurrentTrack().getInfo().title, false);

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }

    private class Stop extends Command {

        public Stop() {
            super(new CommandInfos("stop",
                    "Clear the queue and disconnect from voice channel.",
                    "music stop",
                    false,
                    ""),
                Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) {
            if (!isUserSameChannel(msg)) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You must be in the same channel as me to use this command !")).queue();
                return;
            }

            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());

            manager.stop();
            manager.disconnect();

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Stopping", CommandUtils.ICON_MUSIC);
            builder.addSection("Clearing queue and disconnecting...");

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }

    private class Repeat extends Command {

        public Repeat() {
            super(new CommandInfos("repeat",
                    "Queue the current track to hear it again.",
                    "music repeat",
                    false,
                    ""),
                Permission.MESSAGE_WRITE, Permission.VOICE_SPEAK);
        }

        @Override
        public void execute(Message msg, String[] args) {
            if (!isUserSameChannel(msg)) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You must be in the same channel as me to use this command !")).queue();
                return;
            }

            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());
            manager.playTrack(manager.getCurrentTrack().makeClone());

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Queued `" + manager.getCurrentTrack().getInfo().title + "`", CommandUtils.ICON_MUSIC);

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }

    private class Volume extends Command {

        public Volume() {
            super(new CommandInfos("volume,v",
                    "Set the volume of the bot.",
                    "music volume [volume]",
                    false,
                    ""),
                Permission.MESSAGE_WRITE, Permission.VOICE_SPEAK);
        }

        @Override
        public void execute(Message msg, String[] args) {
            if (!isUserSameChannel(msg)) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You must be in the same channel as me to use this command !")).queue();
                return;
            }

            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());

            if (args.length <= 0) {
                EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                builder.setTitleWithIcon("Volume: " + manager.getVolume() + "%", CommandUtils.ICON_MUSIC);

                msg.getChannel().sendMessage(builder.build()).queue();
            } else {
                try {
                    int volume = Integer.parseInt(args[0]);
                    manager.setVolume(volume);

                    EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                    builder.setTitleWithIcon("Volume set to " + manager.getVolume() + "%", CommandUtils.ICON_MUSIC);

                    msg.getChannel().sendMessage(builder.build()).queue();
                } catch (NumberFormatException e) {
                    msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "The volume must be a number !")).queue();
                }
            }
        }
    }

    private class Pause extends Command {

        public Pause() {
            super(new CommandInfos("pause",
                    "Pause/Resume the player.",
                    "music pause",
                    false,
                    ""),
                Permission.MESSAGE_WRITE, Permission.VOICE_SPEAK);
        }

        @Override
        public void execute(Message msg, String[] args) {
            if (!isUserSameChannel(msg)) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You must be in the same channel as me to use this command !")).queue();
                return;
            }

            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());

            boolean todo = !manager.isPaused();
            manager.setPaused(todo);

            if (todo) {
                EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                builder.setTitleWithIcon("Pausing player", CommandUtils.ICON_MUSIC);

                msg.getChannel().sendMessage(builder.build()).queue();
            } else {
                EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                builder.setTitleWithIcon("Resuming player", CommandUtils.ICON_MUSIC);

                msg.getChannel().sendMessage(builder.build()).queue();
            }
        }
    }

    private class Queue extends Command {

        public Queue() {
            super(new CommandInfos("queue",
                    "Get the current queue.",
                    "music queue",
                    false,
                    ""),
                Permission.MESSAGE_WRITE);
        }

        private String getDuration(long millis) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis - 3600000);
            return (c.get(Calendar.HOUR) < 10 ? "0" + c.get(Calendar.HOUR) : String.valueOf(c.get(Calendar.HOUR))) + ":"
                    + (c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : String.valueOf(c.get(Calendar.MINUTE))) + ":"
                    + (c.get(Calendar.SECOND) < 10 ? "0" + c.get(Calendar.SECOND) : String.valueOf(c.get(Calendar.SECOND)));
        }

        @Override
        public void execute(Message msg, String[] args) {
            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Current queue", CommandUtils.ICON_MUSIC);
            if (!manager.getQueue().isEmpty()) {
                manager.getQueue().forEach(track ->
                    builder.addSection("**" + track.getInfo().title + "** [`" + getDuration(track.getDuration()) + "`]")
                );
            } else {
                builder.addSection("No track queued ! Add one with `" + CommandUtils.PREFIX + "music play <url>`");
            }

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }

    private class Current extends Command {

        private static final String baseUrlVideo = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id={ID}&key=AIzaSyDAPlMGxm7RbG7j01vWA6s2wNkrzzPRbBw";
        private static final String baseUrlChannel = "https://www.googleapis.com/youtube/v3/channels?part=snippet&id={ID}&key=AIzaSyDAPlMGxm7RbG7j01vWA6s2wNkrzzPRbBw";

        private ObjectMapper mapper;

        public Current() {
            super(new CommandInfos("current",
                    "Get infos about the current track.",
                    "music current",
                    false,
                    ""),
                Permission.MESSAGE_WRITE);
            mapper = new ObjectMapper();
        }

        @Override
        public void execute(Message msg, String[] args) {
            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());

            AudioTrack track = manager.getCurrentTrack();

            if (track == null) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "No track playing !")).queue();
                return;
            }

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Current Track:", CommandUtils.ICON_MUSIC);
            builder.setTitle(track.getInfo().title);
            builder.addSection("By **" + track.getInfo().author + "**");

            if (track.getSourceManager().getSourceName().equalsIgnoreCase("youtube")) {
                try {
                    JsonNode nodeVideo = mapper.readValue(new URL(baseUrlVideo.replace("{ID}", track.getIdentifier())), JsonNode.class);
                    String channelId = nodeVideo.get("items").get(0).get("snippet").get("channelId").asText("unknow");
                    String videoFullRes = nodeVideo.get("items").get(0).get("snippet").get("thumbnails").get("maxres").get("url").asText();

                    JsonNode nodeChannel = mapper.readValue(new URL(baseUrlChannel.replace("{ID}", channelId)), JsonNode.class);
                    String channelFullRes = nodeChannel.get("items").get(0).get("snippet").get("thumbnails").get("high").get("url").asText();

                    builder.setThumbnail(channelFullRes);
                    builder.setImage(videoFullRes);
                } catch (IOException e) {
                    new BotException(ExceptionGravity.ERROR, "Error while parsing data from youtube !", e);
                } finally {
                    builder.addSection("Source: [**Youtube**](https://youtu.be/" + track.getIdentifier() + ")");
                }
            } else {
                builder.addSection("Source: **" + track.getSourceManager().getSourceName() + "**");
            }

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }

    private class Shuffle extends Command {

        public Shuffle() {
            super(new CommandInfos("shuffle",
                    "Randomize queued songs.",
                    "music shuffle",
                    false,
                    ""),
                Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) {
            GuildMusicManager manager = HunhowExAPI.getMusicManager().getMusicManager(msg.getGuild());

            manager.shuffle();

            EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
            builder.setTitleWithIcon("Shuffling queue...", CommandUtils.ICON_MUSIC);

            msg.getChannel().sendMessage(builder.build()).queue();
        }
    }
}
