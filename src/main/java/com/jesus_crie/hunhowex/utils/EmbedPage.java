package com.jesus_crie.hunhowex.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmbedPage {

    private List<String> lines;

    public EmbedPage(HashMap<String, String> values) {
        lines = new ArrayList<>();
        values.forEach((key, value) -> {
            lines.add("**" + key + ":** " + value);
        });
    }

    public EmbedPage(String raw) {
        lines = new ArrayList<>();
        lines.add(raw);
    }

    @Override
    public String toString() {
        return String.join("\n", lines);
    }
}
