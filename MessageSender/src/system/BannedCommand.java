package system;

import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BannedCommand implements CommandExecutor {

    private final core plugin;
    public BannedCommand(core main){
        plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[1]);
        PermissionUser user = PermissionsEx.getUser(player.getName());

        if (player.hasPermission("staff.banned.cmd")) {
            if (args[0].isEmpty()) {
                player.sendMessage(plugin.color("&c[BannedSystem] &7Usage : /banned <add-remove-reset> <player>"));
                return false;
            }
            if (args.length < 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (player.hasPermission("staff.banned.cmd.add")) {

                        if (target != null) {
                            File ordner = new File(plugin.getDataFolder().getPath());
                            File file = new File(plugin.getDataFolder().getPath() + "//BlackList//" + target.getUniqueId() + ".yml");

                            if (!ordner.exists()) {
                                ordner.mkdir();
                            }

                            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

                            if (!file.exists()) {
                                try {
                                    file.createNewFile();

                                    config.set("name", target.getName());
                                    config.set("uuid", target.getUniqueId().toString());
                                    config.set("banned", true);
                                    config.set("from", player.getName());
                                    config.set("from-uuid", player.getUniqueId().toString());

                                    Bukkit.broadcastMessage(plugin.color("&c[BannedSystem] &6" + config.getString("name") + " has been added to blacklist by &e" + player.getName()));

                                    /* Discord log */
                                    PermissionUser tuser = PermissionsEx.getUser(target.getName());
                                    String rank = tuser.getParentIdentifiers().get(0);

                                    DateFormat formattedDate = new SimpleDateFormat("yyyy - MMM dd | HH:mm:ss");
                                    Date date = new Date();
                                    String currentData = formattedDate.format(date);

                                    EmbedBuilder embed = new EmbedBuilder();
                                    embed.setColor(new Color(206, 0, 0));
                                    embed.setDescription(config.getString("name") + " **Added to BlackList**");
                                    embed.addField("Date", currentData, false);
                                    embed.addField("uuid", config.getString("uuid"), false);
                                    embed.addField("rank", rank, false);

                                    plugin.getJda().getTextChannelById("730174243752509481").sendMessage(embed.build()).queue();

                                } catch (IOException e) {

                                }
                            } else {
                                player.sendMessage(plugin.color("&c[BannedSystem] &7this player is already in blacklist"));
                            }

                            try {
                                config.save(file);
                            } catch (IOException eex) {
                                eex.printStackTrace();
                            }
                        } else {
                            player.sendMessage(plugin.color("&c[BannedSystem] &7this player is undefined"));
                        }
                    }else player.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + plugin.getStringUtils().getNoPermission()));
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    if (player.hasPermission("staff.banned.cmd.remove")) {
                        if (target != null) {
                            File ordner = new File(plugin.getDataFolder().getPath());
                            File file = new File(plugin.getDataFolder().getPath() + "//BlackList//" + target.getUniqueId() + ".yml");

                            if (!ordner.exists()) {
                                ordner.mkdir();
                            }

                            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

                            if (file.exists()) {

                                file.delete();
                                Bukkit.broadcastMessage(plugin.color("&c[BannedSystem] &6" + config.getString("name") + " has been removed to blacklist by &e" + player.getName()));

                                /* Discord log */
                                PermissionUser tuser = PermissionsEx.getUser(target.getName());
                                String rank = tuser.getParentIdentifiers().get(0);

                                DateFormat formattedDate = new SimpleDateFormat("yyyy - MMM dd | HH:mm:ss");
                                Date date = new Date();
                                String currentData = formattedDate.format(date);

                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setColor(new Color(206, 0, 0));
                                embed.setDescription(config.getString("name") + " **Removed to BlackList**");
                                embed.addField("Date", currentData, false);
                                embed.addField("uuid", "> " + config.getString("uuid"), false);
                                embed.addField("rank", rank, false);

                                plugin.getJda().getTextChannelById("730174243752509481").sendMessage(embed.build()).queue();

                            }else {
                                player.sendMessage(plugin.color("&c[BannedSystem] &7this player is undefined"));
                                return false;
                            }
                        } else {
                            player.sendMessage(plugin.color("&c[BannedSystem] &7this player is undefined"));
                        }
                    }else player.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + plugin.getStringUtils().getNoPermission()));
                }
            }
        }else player.sendMessage(plugin.color(plugin.getStringUtils().getPrefix() + plugin.getStringUtils().getNoPermission()));

        return true;
    }

}
