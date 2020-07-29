package system.MinecraftCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import system.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlacklistCommands implements CommandExecutor, TabCompleter {

    private final core plugin;

    public BlacklistCommands(core plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        String prefix = plugin.getInstance().getStringUtils().getPrefix();

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.color(plugin.getInstance().getStringUtils().getPrefix() + "&conly administration and use this command in the server!"));
            return true;
        }

        if (sender instanceof Player) {
            if (args.length < 1) {
                sender.sendMessage(plugin.color(prefix + "&cUsage: /blacklist_word <commands>"));
                return false;
            }
            if (args.length < 2) {
                if (args[0].equalsIgnoreCase("list")) {
                    String s = "";
                    String[] text = plugin.getInstance().getBlackListFile().getStringList("BlackList.words").toArray(new String[0]);
                    for (int i = 0; i < text.length; i++) {
                        if (i == 0) {
                            s += text[i];
                        } else {
                            s += " / " + text[i];
                        }
                    }
                    sender.sendMessage(plugin.color(prefix + "&6Blacklist words&7: " + s + " &8(&e&l!&8)"));
                    return false;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(plugin.color(prefix + "&dConfig has been reloaded &8(&a&k&l!&8)"));
                    plugin.getInstance().getBlackListFile().reload();
                    return false;
                }
            }
            if (args.length < 3) {

                if (args[0].equalsIgnoreCase("settings")) {
                    if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("true")) {
                        if (plugin.getBlackListFile().getConfiguration().getConfigurationSection("BlackList").getBoolean("available")) {
                            sender.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + "&cblack list is already enabled"));
                            return false;
                        }else {
                            plugin.getBlackListFile().getConfiguration().getConfigurationSection("BlackList").set("available", true);
                            sender.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + "&ablack list has been enabled"));

                            try {
                                plugin.getBlackListFile().getConfiguration().save(plugin.getBlackListFile().getFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    }else
                    if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("false")) {
                        if (plugin.getBlackListFile().getConfiguration().getConfigurationSection("BlackList").getBoolean("available")) {
                            plugin.getBlackListFile().getConfiguration().getConfigurationSection("BlackList").set("available", false);
                            sender.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + "&ablack list has been disabled"));

                            try {
                                plugin.getBlackListFile().getConfiguration().save(plugin.getBlackListFile().getFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }else {
                            sender.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + "&cblack list is already disabled"));
                            return false;
                        }
                    }
                }

                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    builder.append(args[i]).append("");
                }
                String word = builder.toString();

                if (word.isEmpty()) {
                    sender.sendMessage(plugin.color(plugin.getInstance().getStringUtils().getPrefix() + "&cyou should to type the word &8(&e&k&l!&8)")) ;
                    return false;
                }

                if (word.chars().count() > 16) {
                    sender.sendMessage(plugin.color(prefix + "&cthis word is more then 16 chars &8(&e&k&l!&8)"));
                    return false;
                }

                if (args[0].equalsIgnoreCase("add")) {

                    final Pattern regex = Pattern.compile("[<>!@#$%^&*()\\]\\[[-];_:{}?/.,|+='\"]");
                    final Matcher matcher = regex.matcher(word);
                    if (matcher.find()) {
                        sender.sendMessage(plugin.color(prefix + "&cyou can't use &e" + matcher.group(matcher.groupCount()) + " &cin blacklist words &8(&e&k&l!&8)"));
                        return false;
                    }

                    if (plugin.getInstance().getBlackListFile().getConfiguration().getStringList("BlackList.words").contains(word.toLowerCase())) {
                        sender.sendMessage(plugin.color(prefix + "&c" + word + " &7is already in blacklist words &8(&c&k&l!&8)"));
                        return false;
                    }

                    final List<String> blacklist = plugin.getInstance().getBlackListFile().getStringList("BlackList.words");
                    blacklist.add(word.toLowerCase());

                    plugin.getInstance().getBlackListFile().getConfiguration().set("BlackList.words", blacklist);

                    try {
                        plugin.getInstance().getBlackListFile().getConfiguration().save(plugin.getInstance().getBlackListFile().getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    sender.sendMessage(plugin.color(plugin.getInstance().getStringUtils().getPrefix() + "&c" + word + " &7added to blacklist word &8(&a&k&l!&8)"));
                    return true;
                }else if (args[0].equalsIgnoreCase("remove")) {

                    final Pattern regex = Pattern.compile("[<>!@#$%^&*()\\]\\[[-];_:{}?/.,|+='\"]");
                    final Matcher matcher = regex.matcher(word);
                    if (matcher.find()) {
                        sender.sendMessage(plugin.color(prefix + "&cyou can't use &e" + matcher.group(matcher.groupCount()) + " &cin blacklist words &8(&e&l!&8)"));
                        return false;
                    }

                    if (!(plugin.getInstance().getBlackListFile().getConfiguration().getStringList("BlackList.words").contains(word))) {
                        sender.sendMessage(plugin.color(plugin.getInstance().getStringUtils().getPrefix() + "&c" + word + " &7is not in blacklist words &8(&c&k&l!&8)"));
                        return false;
                    }

                    final List<String> blacklist = plugin.getInstance().getBlackListFile().getStringList("BlackList.words");
                    blacklist.remove(word);

                    plugin.getInstance().getBlackListFile().getConfiguration().set("BlackList.words", blacklist);

                    try {
                        plugin.getInstance().getBlackListFile().getConfiguration().save(plugin.getInstance().getBlackListFile().getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    sender.sendMessage(plugin.color(plugin.getInstance().getStringUtils().getPrefix() + "&c" + word + " &7removed to blacklist word &8(&a&k&l!&8)"));
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        String[] ss = {};
        if (args.length < 3) {
            if (args[0].equalsIgnoreCase("remove")){
                ss = plugin.getInstance().getBlackListFile().getStringList("BlackList.words").toArray(new String[0]);
            }
        }
        return Arrays.asList(ss);
    }
}
