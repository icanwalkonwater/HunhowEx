package com.jesus_crie.hunhowex.javadoc;

import org.jsoup.nodes.Element;

public class DocConstructor {

    public DocConstructor(Element row) {
        String rawArgs = row.select("code").text();


    }
}
