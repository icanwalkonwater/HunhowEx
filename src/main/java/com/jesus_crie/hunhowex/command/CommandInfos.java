package com.jesus_crie.hunhowex.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandInfos {

    private String name;
    private List<String> alisases;
    private String description;
    private List<String> usage;
    private Boolean hidden;
    private List<String> serverOnly;

    public CommandInfos(String aliases, String description, String usage, Boolean hidden, String serverOnly) {
        this.alisases = Arrays.asList(aliases.split(","));
        this.name = this.alisases.get(0);
        this.description = description;
        this.usage = Arrays.asList(usage.split(","));
        this.hidden = hidden;

        this.serverOnly = new ArrayList<>();
        if (!(serverOnly.length() <= 0))
            this.serverOnly = Arrays.asList(serverOnly.split(","));
    }

    public String getName() {
        return name;
    }

    public String getNameCapitalized() {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public String getDescription() {
        return description;
    }

    public List<String> getUsage() {
        return usage;
    }

    public boolean isHidden() {
        return hidden == null ? false : hidden;
    }

    public List<String> getServerOnly() {
        return serverOnly;
    }

    public boolean isServerOnly() {
        return !serverOnly.isEmpty();
    }

    public List<String> getAlisases() {
        return alisases;
    }

    public boolean hasAliases() {
        return alisases.isEmpty();
    }
}
