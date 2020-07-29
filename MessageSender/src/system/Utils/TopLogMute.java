package system.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import system.core;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TopLogMute {

    private final core plugin;

    /* Main OPTIONS */
    private String leaderboard;
    private File file;
    private YamlConfiguration config;
    private static LinkedHashMap<UUID, Integer> playerList = new LinkedHashMap<>();

    /* Addons */
    public static int count;
    public String PLAYER_PLACE;

    public TopLogMute(core core) {
        this.plugin = core;
        this.file = new File("plugins//SenderMessage//Mute//mutelog.yml");
        this.config = config.loadConfiguration(file);
    }

    public void createTop(Player player) {
        final AtomicInteger place = new AtomicInteger(0);
        getTop().forEach(((uuid, number) -> {
            int playerPlace = place.addAndGet(1);

            playerList.put(UUID.fromString(uuid),playerPlace);
            player.sendMessage(plugin.color("&e#" + playerPlace + "&r &b" + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + "&r &l|&r &eMute Log : &b" + number));
        }));
    }

    public void createTopInventory(Inventory inv) {
        final AtomicInteger place = new AtomicInteger(0);
        getTop().forEach(((uuid, number) -> {
            int playerPlace = place.addAndGet(1);

            ItemStack playerItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) playerItem.getItemMeta();
            meta.setDisplayName(core.color( "&d" + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + "&r | &e#" + playerPlace));
            meta.setOwner(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());

            List<String> list = new ArrayList<>();
            list.add(plugin.color("&d&m&l-&e&m&l------------------------------------&d&m&l-"));
            list.add(plugin.color("&dPlayer :&r &e" + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()));
            list.add(plugin.color("&dPlayer place :&r &e#" + playerPlace));
            list.add(plugin.color("&dMuted time :&r &e" + plugin.getMuteFile().getConfiguration().getConfigurationSection("Users").getStringList(uuid).size()));
            list.add(plugin.color("&d&m&l-&e&m&l------------------------------------&d&m&l-"));
            list.add(plugin.color("&eMute log : "));
            list.add(" ");
            list.addAll(plugin.color(plugin.getMuteFile().getConfiguration().getConfigurationSection("Users").getStringList(uuid)));

            meta.setLore(list);
            playerItem.setItemMeta(meta);
            inv.setItem(playerPlace-1, playerItem);
        }));
    }

    private HashMap<String, Integer> getTop() {
        final Map<String, Integer> users = new HashMap<>();
        final ConfigurationSection configList = plugin.getMuteFile().getConfiguration().getConfigurationSection("Users");
        configList.getValues(false)
                .forEach((key, value) ->{
                    List<String> sizeList = config.getConfigurationSection("Users").getStringList(key);
                    users.put(key, sizeList.size());
                        });

        final HashMap<String, Integer> topUsers = (users)
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (key, value) -> value, LinkedHashMap::new));
        return topUsers;
    }

    public static int getCount() {
        return count;
    }

    public static int getPlace(UUID uuid) {
        return playerList.get(uuid);
    }
}
