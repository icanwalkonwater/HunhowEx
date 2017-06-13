package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.javadoc.DocClass;
import com.jesus_crie.hunhowex.exception.BotException;
import com.jesus_crie.hunhowex.exception.ExceptionGravity;
import com.jesus_crie.hunhowex.logger.Logger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JavaDocCommand extends Command {

    private static final HashMap<String, DocClass> classes = new HashMap<>();

    public JavaDocCommand() {
        super(new CommandInfos("javadoc,doc,java",
                "Get the javadoc of a specific class or function.",
                "javadoc <Class>,javadoc <Class#method>,javadoc <Class#method(args)>",
                true,
                "264001800686796800"),
            Permission.MESSAGE_WRITE);
        init();
    }

    public static void init() {
        Logger.info("[Javadoc] Loading javadoc...");
        try {
            Document doc = Jsoup.connect(DocClass.docAllClasses).get();
            List<String> links = doc.getElementsByClass("indexContainer").get(0).getElementsByTag("a")
                    .stream()
                    .map(e -> e.attr("href"))
                    .collect(Collectors.toList());


        } catch (IOException e) {
            new BotException(ExceptionGravity.ERROR, "Failed to load javadoc !", e);
        }
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {

    }
}
