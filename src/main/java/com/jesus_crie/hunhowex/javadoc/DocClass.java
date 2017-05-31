package com.jesus_crie.hunhowex.javadoc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocClass {

    public static final String docAllClasses = "http://docs.oracle.com/javase/8/docs/api/allclasses-frame.html";
    public static final String baseAPI = "http://docs.oracle.com/javase/8/docs/api/";

    private String url;
    private String name;
    private Class clazz;

    private List<String> generalComment;
    private List<DocConstructor> constructors;
    private List<DocMethod> methods;

    public DocClass(String url) throws ClassNotFoundException {
        this.url = url;

        String cleanUrl = url.replace(".html", "")
                .replace(".", "$")
                .replace("/", ".");
        clazz = Class.forName(cleanUrl);

        name = clazz.getSimpleName();
        Class enclosing = clazz.getEnclosingClass();
        while (enclosing != null) {
            name = enclosing.getSimpleName() + "$" + name;
            enclosing = enclosing.getEnclosingClass();
        }

        try {
            Document datas = Jsoup.connect(baseAPI + url).get();

            Element classComment = datas.select("div.contentContainer > div.description > ul.blockList > li.blockList > div.block").first();
            getMainComment(classComment);

            if (!clazz.isInterface()) {
                Element tableConstructors = datas.select("a[name=constructor.summary]").first().parent().select("table.memberSummary").first();
                constructors = DocConstructors.get(tableConstructors);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // TODO log
        }
    }

    public String getName() {
        return name;
    }

    private void getMainComment(Element div) {
        String ugly = div.html();
        generalComment = Arrays.asList(ugly
                .replaceAll("\n", "")
                .replaceAll("<a [^>]*>|</a>", "")
                .replaceAll("<tt>|</tt>|<code>|</code>", "`")
                .replaceAll("<code>|</code>", "`")
                .replaceAll("<i>|</i>|<em>|</em>", "_")
                .replaceAll("<pre>", "```java\n")
                .replaceAll("</pre>", "```")
                .replaceAll("<strong>|</strong>", "*")
                .split("<p>|</p>"));
    }
}
