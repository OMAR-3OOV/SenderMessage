package system.MinecraftCommand;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import system.Utils.TopLogMute;
import system.core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MuteCommands implements CommandExecutor, TabCompleter {

    private final core plugin;

    public MuteCommands(core core) {
        this.plugin = core;
    }

    public core getPlugin() {
        return plugin;
    }

    public Inventory mutedPlayer = Bukkit.createInventory(null, 54, core.color("&cLeaderBoard Muted"));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = plugin.getStringUtils().getPrefix();

        if (!(sender instanceof Player)) {
            sender.sendMessage(core.color(prefix + "&conly staff and player can do this commands"));
            return false;
        }

        if (sender instanceof Player) {
            if(args.length < 2) {
                TopLogMute topLogMute = new TopLogMute(plugin.getInstance());
                if (args[0].equalsIgnoreCase("leaderboard")) {

                    sender.sendMessage(plugin.color("      &d&l* &d&n&lLeaderboard muted log&r &d&l* &b( all muted : " + plugin.getMuteFile().getConfiguration().getConfigurationSection("Users").getKeys(false).size() + " &b)"));
                    sender.sendMessage(plugin.color("&d&m&l-&b&m&l-------------------------------------&d&m&l-"));
                    topLogMute.createTop(((Player) sender).getPlayer());
                    sender.sendMessage(plugin.color("&d&m&l-&b&m&l-------------------------------------&d&m&l-"));

                    return false;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(plugin.color(prefix + "&dConfig has been reloaded &8(&a&k&l!&8)"));
                    plugin.getInstance().getMuteFile().reload();
                    return false;
                }
                if (args[0].equalsIgnoreCase("inventory")) {
                    ((Player) sender).openInventory(mutedPlayer);
                    topLogMute.createTopInventory(mutedPlayer);
                }
            }
            if (args.length < 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    OfflinePlayer targetOfflinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                    if (plugin.getBlackListFile().getConfiguration().getStringList("Users").contains(targetOfflinePlayer.getUniqueId().toString())) {
                        sender.sendMessage(core.color(prefix + "&cthis player currently in mute list &8(&c&k&l!&8)"));
                        return false;
                    }

                    DateFormat formattedDate = new SimpleDateFormat("yyyy - MMM dd | HH:mm:ss");
                    Date date = new Date();
                    String currentData = formattedDate.format(date);

                    List<String> list = plugin.getInstance().getMuteFile().getConfiguration().getStringList("Users."+targetOfflinePlayer.getUniqueId().toString());
                    list.add("Staff muted by : " + sender.getName() + " | " + currentData);

                    plugin.getInstance().getMuteFile().getConfiguration().set("Users."+targetOfflinePlayer.getUniqueId().toString(), list);

                    try {
                        plugin.getInstance().getMuteFile().getConfiguration().save(plugin.getInstance().getMuteFile().getMuteFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   
                    final List<String> user = plugin.getBlackListFile().getConfiguration().getStringList("Users");
                    user.add(targetOfflinePlayer.getUniqueId().toString());

                    plugin.getBlackListFile().getConfiguration().set("Users", user);

                    try {
                        plugin.getBlackListFile().getConfiguration().save(plugin.getInstance().getBlackListFile().getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    sender.sendMessage(core.color(prefix + args[1] + " &chas been muted &8(&a&k&l!&8)"));
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    OfflinePlayer targetOfflinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                    if (!plugin.getBlackListFile().getConfiguration().getStringList("Users").contains(targetOfflinePlayer.getUniqueId().toString())) {
                        sender.sendMessage(core.color(prefix + "&cthis player isn't in mute list &8(&c&k&l!&8)"));
                        return false;
                    }

                    final List<String> user = plugin.getBlackListFile().getConfiguration().getStringList("Users");
                    user.remove(targetOfflinePlayer.getUniqueId().toString());

                    plugin.getBlackListFile().getConfiguration().set("Users", user);

                    try {
                        plugin.getBlackListFile().getConfiguration().save(plugin.getInstance().getBlackListFile().getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bukkit.broadcastMessage(core.color(prefix + targetOfflinePlayer.getName() + " &cremoved from mute list &8(&a&k&l!&8)"));
                }
                if (args[0].equalsIgnoreCase("check")) {
                    OfflinePlayer targetOfffline = Bukkit.getServer().getOfflinePlayer(args[1]);

                    if (plugin.getMuteFile().getConfiguration().getConfigurationSection("Users") == null) {
                        sender.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + "&cUsers list is empty &7(&c&K&l!&7)"));
                        return false;
                    }

                    if (plugin.getMuteFile().getConfiguration().getConfigurationSection("Users").getKeys(false).contains(targetOfffline.getUniqueId().toString())) {
                        List<String> list = new ArrayList<>(plugin.getInstance().getMuteFile().getConfiguration().getStringList("Users." + targetOfffline.getUniqueId().toString()));

                        int total = list.size();
                        int startIndex = total - 5;

                        String type = "";
                        if (!plugin.getBlackListFile().checkPlayerMuted(targetOfffline.getUniqueId().toString())) {
                            type = "&aIntact";
                        } else {
                            type = "&cMuted";
                        }
                        if (plugin.getInstance().getMuteFile().getConfiguration().getConfigurationSection("Users").getStringList(targetOfffline.getUniqueId().toString()).isEmpty()) {
                            sender.sendMessage(plugin.color(prefix + "&7( &e" + targetOfffline.getName() + " " + type + " &7)\n&r" + "&e&m&l----------------------------" + "\n&r     " +
                                    String.join("\n     ", "&cnothing found in &e" + targetOfffline.getName() + " &clog")));
                            sender.sendMessage(plugin.color("&e&m&l----------------------------"));
                            return false;
                        }

                        if (plugin.getInstance().getMuteFile().getConfiguration().getStringList("Users."+targetOfffline.getUniqueId().toString()).size() > 5) {
                            sender.sendMessage(plugin.color(prefix + "&7( &e" + targetOfffline.getName() + " " + type + " &7)\n&r" + "&e&m&l----------------------------" + "\n&r- " +
                                    String.join("\n- ", plugin.getInstance().getMuteFile().getConfiguration().getStringList("Users." + targetOfffline.getUniqueId().toString()).subList(startIndex, total))));
                            sender.sendMessage(plugin.color("&e&m&l----------------------------"));
                        }else {
                            sender.sendMessage(plugin.color(prefix + "&7( &e" + targetOfffline.getName() + " " + type + " &7)\n&r" + "&e&m&l----------------------------" + "\n&r- " +
                                    String.join("\n- ", plugin.getInstance().getMuteFile().getConfiguration().getStringList("Users." + targetOfffline.getUniqueId().toString()).subList(0, total))));
                            sender.sendMessage(plugin.color("&e&m&l----------------------------"));
                        }
                    }else {
                        sender.sendMessage(plugin.color(prefix + "&cthis player is not found"));
                    }
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    OfflinePlayer targetOfffline = Bukkit.getServer().getOfflinePlayer(args[1]);

                    if (plugin.getMuteFile().getConfiguration().getConfigurationSection("Users").contains(targetOfffline.getUniqueId().toString())) {
                        List<String> ClearList = new ArrayList<>(plugin.getInstance().getMuteFile().getConfiguration().getStringList("Users."+targetOfffline.getUniqueId().toString()));
                        ClearList.clear();

                        plugin.getInstance().getMuteFile().getConfiguration().getConfigurationSection("Users").set(targetOfffline.getUniqueId().toString(), ClearList);

                        try {
                            plugin.getInstance().getMuteFile().getConfiguration().save(plugin.getInstance().getMuteFile().getMuteFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        sender.sendMessage(plugin.color(prefix + targetOfffline.getName() + " &alog has been reset &8(&a&k&l!&8)"));
                    }else {
                        sender.sendMessage(plugin.color(prefix + "&cthis player doesn't longer in the log &8(&c&k&l!&8)"));
                    }
                }
                if (args[0].equalsIgnoreCase("test")) {
                    if (args[1].isEmpty()) {
                        sender.sendMessage("nothing found");
                    }
                    OfflinePlayer targetOfflinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                    if (plugin.getBlackListFile().getConfiguration().getStringList("Users").contains(targetOfflinePlayer.getUniqueId().toString())) {
                        sender.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + targetOfflinePlayer.getName() + " player is found"));
                    }else sender.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + targetOfflinePlayer.getName() + " player is not found"));

                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        String[] users = {};
        if (args.length < 2) {

            List<String> list = new ArrayList<>();
            list.add("add");
            list.add("remove");
            list.add("check");
            users = list.toArray(new String[0]);
        }
        if (args.length < 3) {
            if (args[0].equalsIgnoreCase("remove")) {
                users = plugin.getBlackListFile().getConfiguration().getStringList("Users").toArray(new String[0]);
            }
            if (args[0].equalsIgnoreCase("add")) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    List<String> list = Collections.singletonList(online.getName());

                    users = list.toArray(new String[0]);
                }
            }
        }
        return Arrays.asList(users);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> getHighestToLowestValue(Map<K, V> map) {
        Stream<Map.Entry<K, V>> stream = map.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder()));

        stream = stream.limit(1);

        return stream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));
    }

}
