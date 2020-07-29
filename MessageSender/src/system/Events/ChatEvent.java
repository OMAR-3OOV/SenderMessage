package system.Events;

import net.dv8tion.jda.api.entities.Webhook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import system.core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class ChatEvent<count> implements Listener {

    private Integer count = 0;
    private LinkedHashMap<UUID, count> warning = new LinkedHashMap<>();

    private final core plugin;

    public ChatEvent(core plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        String prefix = plugin.getInstance().getStringUtils().getPrefix();

        if (plugin.getInstance().getBlackListFile().getStringList("Users").contains(event.getPlayer().getUniqueId().toString())) {
            event.getPlayer().sendMessage(plugin.color(prefix + "&cyou currently muted &8(&c&k&l!&8)"));
            event.setCancelled(true);
            return;
        }

        if (plugin.getBlackListFile().getConfiguration().getConfigurationSection("BlackList").getBoolean("available") == false) {
            event.setCancelled(false);
            return;
        }

        for (String textMessage : event.getMessage().split(" ")) {
            if (plugin.getBlackListFile().getConfiguration().getConfigurationSection("BlackList").getStringList("words").contains(textMessage.toLowerCase())) {

                event.setCancelled(true);
                event.getPlayer().sendMessage(plugin.color(prefix + "&cthis message contains inappropriate words"));

                if (count < 3) {
                    event.getPlayer().sendMessage(plugin.color(prefix + "&dif you send more message contains inappropriate words you well get mute automatically"));
                } else if (count >= 3) {

                       DateFormat formattedDate = new SimpleDateFormat("yyyy - MMM dd | HH:mm:ss");
                       Date date = new Date();
                       String currentData = formattedDate.format(date);

                       if (plugin.getInstance().getMuteFile().getConfiguration().getConfigurationSection("Users").contains(event.getPlayer().getUniqueId().toString())) {

                           List<String> list = plugin.getInstance().getMuteFile().getConfiguration().getStringList("Users." + event.getPlayer().getUniqueId().toString());
                           list.add("Text: " + event.getMessage() + " | " + currentData);

                           plugin.getInstance().getMuteFile().getConfiguration().set("Users." + event.getPlayer().getUniqueId().toString(), list);
                           try {
                               plugin.getInstance().getMuteFile().getConfiguration().save(plugin.getInstance().getMuteFile().getMuteFile());
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }

                       List<String> list = plugin.getInstance().getMuteFile().getConfiguration().getStringList("Users." + event.getPlayer().getUniqueId().toString());
                       list.add("Text: " + event.getMessage() + " | " + currentData);

                       plugin.getInstance().getMuteFile().getConfiguration().set("Users." + event.getPlayer().getUniqueId().toString(), list);

                       try {
                           plugin.getInstance().getMuteFile().getConfiguration().save(plugin.getInstance().getMuteFile().getMuteFile());
                       } catch (IOException e) {
                           e.printStackTrace();
                       }

                       event.getPlayer().sendMessage(plugin.color(prefix + "&cyou get automatically mute &8(&c&k&l!&8)"));
                       final List<String> users = plugin.getInstance().blackListFile.getStringList("Users");
                       users.add(event.getPlayer().getUniqueId().toString());

                       plugin.getInstance().getBlackListFile().getConfiguration().set("Users", users);
                       try {
                           plugin.getInstance().getBlackListFile().getConfiguration().save(plugin.getInstance().getBlackListFile().getFile());
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                }

                if (warning.containsKey(event.getPlayer().getUniqueId())) {
                    count++;
                    return;
                }

                warning.put(event.getPlayer().getUniqueId(), (count) count);
                return;
            }
        }

        /**
         * Discord API send message to discord
         */
        Webhook webhook = null;
        List<Webhook> webhooks = plugin.getJda().getTextChannelById(plugin.getChannelid()).retrieveWebhooks().complete();

        for (int i = 0; i < webhooks.size(); i++) {
            if (webhooks.get(i).getName().equalsIgnoreCase(event.getPlayer().getUniqueId().toString())) {
                webhook = webhooks.get(i);
                break;
            }
        }
        if (webhook == null) {
            webhook = plugin.getJda().getTextChannelById(plugin.getChannelid()).createWebhook(event.getPlayer().getUniqueId().toString()).complete();
        }

        plugin.sendMessageToDiscord(event.getPlayer(), event.getMessage(), webhook);
    }
}
