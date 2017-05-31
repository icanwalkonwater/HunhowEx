package com.jesus_crie.hunhowex.javadoc;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DocConstructors {

    public static List<DocConstructor> get(Element table) {
        List<DocConstructor> constructors = new ArrayList<>();

        Elements lines = table.select("tr[class]");
        lines.forEach(l -> constructors.add(new DocConstructor(l.select("td").first())));

        return constructors;
    }
}
