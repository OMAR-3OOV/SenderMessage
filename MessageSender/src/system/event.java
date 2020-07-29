package system;

import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class event implements Listener {

    private core plugin;

    public event(core main){
        plugin = main;
    }

    @EventHandler
    private void login(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        File file = new File(plugin.getDataFolder().getPath()+"//BlackList//" + player.getUniqueId().toString() +".yml");
        if(!file.exists()){
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (config.getBoolean("banned") == true){
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, plugin.color("&c[BannedSystem] &7you are in blacklisted"));
        }
    }
    @EventHandler
    private void Join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PermissionUser user = PermissionsEx.getUser(player.getName());
        String rank = user.getParentIdentifiers().get(0);

        File file = new File(plugin.getDataFolder().getPath()+"//BlackList//" + player.getUniqueId()+".yml");
        if(!file.exists()){
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (config.getBoolean("banned") == true){
            //player.kickPlayer(plugin.color("&c[BannedSystem] &7you are in blacklisted"));
        }

        DateFormat formattedDate = new SimpleDateFormat("yyyy - MMM dd | HH:mm:ss");
        Date date = new Date();
        String currentData = formattedDate.format(date);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.GREEN);
        embed.setDescription(player.getName() + " **Join the server**");
        embed.addField("Date", currentData, false);
        embed.addField("rank", rank, false);

        plugin.getJda().getTextChannelById("730174417946279964").sendMessage(embed.build()).queue();
    }

    @EventHandler
    private void Join(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        PermissionUser user = PermissionsEx.getUser(player.getName());
        String rank = user.getParentIdentifiers().get(0);

        DateFormat formattedDate = new SimpleDateFormat("yyyy - MMM dd | HH:mm:ss");
        Date date = new Date();
        String currentData = formattedDate.format(date);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setDescription(player.getName() + " **Quit the server**");
        embed.addField("Date", currentData, false);
        embed.addField("rank", rank, false);

        plugin.getJda().getTextChannelById("730174417946279964").sendMessage(embed.build()).queue();
    }

    @EventHandler
    public void kick(PlayerKickEvent event) {
        Player player = event.getPlayer();

        PermissionUser user = PermissionsEx.getUser(player.getName());
        String rank = user.getParentIdentifiers().get(0);

        DateFormat formattedDate = new SimpleDateFormat("yyyy - MMM dd | HH:mm:ss");
        Date date = new Date();
        String currentData = formattedDate.format(date);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(255, 172, 172));
        embed.setDescription(player.getName() + " **Kicked from the server**");
        embed.addField("Date", currentData, false);
        embed.addField("Reason", event.getReason(), false);
        embed.addField("rank", rank, false);

        plugin.getJda().getTextChannelById("730174243752509481").sendMessage(embed.build()).queue();
    }
}
