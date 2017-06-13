package com.jesus_crie.hunhowex.command;

import com.jesus_crie.hunhowex.logger.Logger;
import com.jesus_crie.hunhowex.utils.CommandUtils;
import com.jesus_crie.hunhowex.utils.EmbedMessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class WarframeCommand extends Command {

    public WarframeCommand() {
        super(new CommandInfos("warframe,wf",
                "Get or calculate some informations like armor reduction or price check.",
                "warframe armor [amount]," +
                        "warframe armor <base armor> <base level> <level>," +
                        "warframe health <base health> <base level> <level>," +
                        "warframe affinity <base affinity> <level>," +
                        "warframe raid [player]," +
                        "warframe arcane <name>," +
                        "warframe whereis <prime part>," +
                        "warframe pricecheck <prime part>," +
                        "warframe <alert|invasions|baro|fissures|sortie>",
                false,
                ""),
            Permission.MESSAGE_WRITE);
        super.registerSubCommands(
                new Armor(),
                new Health(),
                new Affinity(),
                new Raid(),
                new Arcane(),
                new WhereIs(),
                new PriceCheck(),
                new Alert(),
                new Invasions(),
                new Baro(),
                new Fissure(),
                new Sortie()
        );
    }

    @Override
    public void execute(Message msg, String[] args) throws PermissionException {
        if (args.length <= 0) {
            msg.getChannel().sendMessage(
                    CommandUtils.getMessageError(msg.getAuthor(), "You need to provide a subcommand ! Use `" + CommandUtils.PREFIX + "help warframe`")).queue();
            return;
        }

        Command sub = super.getSubCommand(args[0]);
        if (sub == null) {
            msg.getChannel().sendMessage(
                    CommandUtils.getMessageError(msg.getAuthor(), "Unknown subcommand ! Use `" + CommandUtils.PREFIX + "help warframe`")).queue();
            return;
        }

        sub.execute(msg, super.cutFirstArg(args));
    }

    private class Armor extends Command {

        public Armor() {
            super(new CommandInfos("armor",
                    "Calculate the amount of armor of an ennemy.",
                    "warframe armor [amount],waframe armor <base armor> <base level> <level>",
                    false,
                    ""),
                Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {
            if (args.length <= 0) {
                EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                builder.setTitleWithIcon("Armor Graph", CommandUtils.ICON_WARFRAME);

                builder.setImage(CommandUtils.WARFRAME_ARMOR_RECAP);

                msg.getChannel().sendMessage(builder.build()).queue();
            } else if (args.length < 3) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You need to provide all arguments !")).queue();
            } else {
                try {
                    int base_armor = Integer.parseInt(args[0]);
                    int base_level = Integer.parseInt(args[1]);
                    int level = Integer.parseInt(args[2]);

                    double armor = Math.floor(base_armor * (1 + Math.pow(level - base_level, 1.75) / 200));
                    double reduction = armor / (300 + armor) * 100;

                    EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                    builder.setTitleWithIcon("Armor Calculation", CommandUtils.ICON_WARFRAME);

                    builder.addField("Enemy infos", "Base armor: **" + base_armor + "** (lvl **" + base_level + "**)\n" +
                            "Level: **" + level + "**", false);
                    builder.addField("Armor", String.valueOf((int) armor), true);
                    builder.addField("Damage Reduction", (int) reduction + "%", true);

                    msg.getChannel().sendMessage(builder.build()).queue();
                } catch (NumberFormatException e) {
                    msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "All arguments must be numbers !")).queue();
                }
            }
        }
    }

    private class Health extends Command {

        public Health() {
            super(new CommandInfos("health",
                            "Calculate the amount of health or shields of an ennemy.",
                            "warframe health <base health> <base level> <level>",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {
            if (args.length < 3) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You need to provide all arguments !")).queue();
            } else {
                try {
                    int base_health = Integer.parseInt(args[0]);
                    int base_level = Integer.parseInt(args[1]);
                    int level = Integer.parseInt(args[2]);

                    double health = base_health + Math.pow(level - base_level, 2) * 0.0075 * base_health;

                    EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                    builder.setTitleWithIcon("Health / Shields Calculation", CommandUtils.ICON_WARFRAME);

                    builder.addField("Enemy infos", "Base health: **" + base_health + "** (lvl **" + base_level + "**)\n" +
                            "Level: **" + level + "**", false);
                    builder.addField("Health / Shields", String.valueOf((int) health), true);

                    msg.getChannel().sendMessage(builder.build()).queue();
                } catch (NumberFormatException e) {
                    msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "All arguments must be numbers !")).queue();
                }
            }
        }
    }

    private class Affinity extends Command {

        public Affinity() {
            super(new CommandInfos("affinity",
                            "Calculate the amount of affinity given by an ennemy.",
                            "warframe affinity <base affinity> <base level> <level>",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {
            if (args.length <= 0) {
                EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                builder.setTitleWithIcon("Affinity Graph", CommandUtils.ICON_WARFRAME);

                builder.setImage(CommandUtils.WARFRAME_AFFINTY_RECAP);

                msg.getChannel().sendMessage(builder.build()).queue();
            } else if (args.length < 3) {
                msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "You need to provide all arguments !")).queue();
            } else {
                try {
                    int base_affinity = Integer.parseInt(args[0]);
                    int base_level = Integer.parseInt(args[1]);
                    int level = Integer.parseInt(args[2]);

                    double affinity = base_affinity * (1 + (0.1425 * Math.pow(level, 0.5)));

                    EmbedMessageBuilder builder = new EmbedMessageBuilder(msg.getAuthor());
                    builder.setTitleWithIcon("Affinity Calculation", CommandUtils.ICON_WARFRAME);

                    builder.addField("Enemy infos", "Base affinity: **" + base_affinity + "** (lvl **" + base_level + "**)\n" +
                            "Level: **" + level + "**", false);
                    builder.addField("Affinity", String.valueOf((int) affinity), true);

                    msg.getChannel().sendMessage(builder.build()).queue();
                } catch (NumberFormatException e) {
                    msg.getChannel().sendMessage(CommandUtils.getMessageError(msg.getAuthor(), "All arguments must be numbers !")).queue();
                }
            }
        }
    }

    private class Raid extends Command {

        public Raid() {
            super(new CommandInfos("raid",
                            "Display raid stats about someone.",
                            "warframe raid <user>",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {

        }
    }

    private class Arcane extends Command {

        public Arcane() {
            super(new CommandInfos("arcane",
                            "Display some informations about an arcane.",
                            "warframe arcane <name>",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {

        }
    }

    private class WhereIs extends Command {

        public WhereIs() {
            super(new CommandInfos("whereis,wi",
                            "Get the drop locations of a prime part.",
                            "warframe whereis <prime part>",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {
            // http://xenogelion.com/Hidden/Relics.json
        }
    }

    private class PriceCheck extends Command {

        public PriceCheck() {
            super(new CommandInfos("pricecheck,pc",
                            "Retrieve the price of an item.",
                            "warframe pricecheck <item>",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {

        }
    }

    private class Alert extends Command {

        public Alert() {
            super(new CommandInfos("alert,alerts",
                            "Display current alerts from WorldState.",
                            "warframe alert",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {

        }
    }

    private class Invasions extends Command {

        public Invasions() {
            super(new CommandInfos("invasions,invasion",
                            "Display current invasions from WorldState.",
                            "warframe invasions",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {

        }
    }

    private class Baro extends Command {

        public Baro() {
            super(new CommandInfos("baro,salt",
                            "Display the inventory of baro from WorldState.",
                            "warframe baro",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {

        }
    }

    private class Fissure extends Command {

        public Fissure() {
            super(new CommandInfos("fissure,fissures",
                            "Display current void fissures from WorldState.",
                            "warframe fissure",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {

        }
    }

    private class Sortie extends Command {

        public Sortie() {
            super(new CommandInfos("sortie",
                            "Display the current sortie from WorldState.",
                            "warframe sortie",
                            false,
                            ""),
                    Permission.MESSAGE_WRITE);
        }

        @Override
        public void execute(Message msg, String[] args) throws PermissionException {

        }
    }
}
