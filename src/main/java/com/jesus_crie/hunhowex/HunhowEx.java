package com.jesus_crie.hunhowex;

import com.jesus_crie.hunhowex.command.*;
import com.jesus_crie.hunhowex.exception.BotException;
import com.jesus_crie.hunhowex.exception.ExceptionGravity;
import com.jesus_crie.hunhowex.listener.*;
import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.storage.JsonConfig;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.ThreadManager;
import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.impl.GameImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.util.Timer;
import java.util.TimerTask;

public class HunhowEx {

    public static final String VERSION = "2.0.0";

    private static HunhowEx instance;
    private static boolean ready = false;

    private JDA jda;

    public HunhowEx(String token) {
        Logger.info("[Start] Starting...");
        instance = this;

        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setAudioEnabled(true)
                    .setAutoReconnect(true)
                    .buildBlocking();
        } catch (LoginException e) {
            new BotException(ExceptionGravity.FATAL, "Token invalid !", LoginException.class);
            System.exit(1);
        } catch (RateLimitedException e) {
            new BotException(ExceptionGravity.ERROR, "I don't know how this is happening", RateLimitedException.class);
        } catch (InterruptedException e) {
            new BotException(ExceptionGravity.FATAL, "Stop interrupting me !", RateLimitedException.class);
        }

        jda.getPresence().setGame(new GameImpl("Starting...", null, Game.GameType.DEFAULT));

        Logger.info("[Start] Initializing Discord logger...");
        Logger.setLogChannel(jda.getTextChannelById("317073384293007360"));

        Logger.info("[Start] Initializing error handler...");
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            new BotException(ExceptionGravity.FATAL, "Unhandled error ! Message: " + e, e);
            e.printStackTrace();
        });

        Logger.info("[Start] Loading config...");
        JsonConfig.init(jda.getGuilds());

        Logger.info("[Start] Initializing API...");
        HunhowExAPI.init();

        Logger.info("[Start] Registering listeners...");
        jda.addEventListener(
                new CommandListener(),
                new MuteListener(),
                new FriendRequestListener(),
                new VoiceListener(),
                new TrollListener()
        );

        Logger.info("[Start] Registering commands...");
        HunhowExAPI.registerCommands(
                // Base commands
                new HelpCommand(),

                // Common commands
                new PingCommand(),
                new MusicCommand(),
                new QuoteCommand(),
                new UserInfoCommand(),
                new ServerInfoCommand(),

                // Management commands
                new InfoCommand(),
                new AddCommand(),
                new InitCommand(),
                new UnregisterCommand(),
                new KickCommand(),
                new BanCommand(),
                new StopCommand(),

                // Dev commands
                new DebugCommand(),
                new TestCommand(),
                new EmbedCommand()
        );
        Logger.info("[Start] " + HunhowExAPI.getCommands().size() + " commands registerd");

        Logger.info("[Start] Enabling music components...");
        HunhowExAPI.getMusicManager().loadAutoPlaylist();
        HunhowExAPI.getMusicManager().registerGuilds(JsonConfig.getGuildConfigs());

        jda.getPresence().setGame(new GameImpl(CommandUtils.PREFIX + "help - v" + VERSION, "https://www.twitch.tv/discordapp", Game.GameType.TWITCH));
        Logger.info("[Start] Done !");
        ready = true;
    }

    public void shutdownTotal() {
        ready = false;
        Logger.info("[Running] Shutting down... (total)");
        HunhowExAPI.getMusicManager().disconnectFromAll();
        ThreadManager.cancelAll();
        JsonConfig.stopAutoSave();
        jda.shutdown(true);
    }

    public void shutdownPartial() {
        ready = false;
        Logger.info("[Running] Shutting down... (partial)");
        HunhowExAPI.getMusicManager().disconnectFromAll();
        ThreadManager.cancelAll();
        jda.shutdown(false);
    }

    //FOR DEV
    public void shutdownTimer() {
        Logger.info("[DEBUG] Shutting down in 20 seconds...");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ready = false;
                HunhowExAPI.getMusicManager().disconnectFromAll();
                shutdownTotal();
            }
        }, 20000);
    }

    public JDA getJda() {
        return jda;
    }

    public static boolean isConnected() {
        return ready;
    }

    public static HunhowEx getInstance() {
        return instance;
    }
}
