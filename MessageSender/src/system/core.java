package system;

import com.sun.xml.internal.ws.resources.SenderMessages;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Webhook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import system.Events.ChatEvent;
import system.MinecraftCommand.BlackListFile;
import system.MinecraftCommand.BlacklistCommands;
import system.MinecraftCommand.MuteCommands;
import system.MinecraftCommand.FilesCreator.MuteFile;
import system.MinecraftCommand.FilesCreator.fileType;
import system.MinecraftCommand.FilesCreator.fileTypeFanction;
import system.Utils.StringsUtils;
import system.Utils.TopLogMute;

import javax.security.auth.login.LoginException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class core extends JavaPlugin implements Listener {

    private JDA jda;
    private core instance;
    private final static long channelid = 730072868146249810L;

    // Utils -> Utils list
    public StringsUtils stringUtils;
    public BlackListFile blackListFile;
    public MuteFile muteFile;
    public fileTypeFanction fileTypeFanction;
    public TopLogMute topLogMute;

    // HashMap ->
    public SenderMessages leaderboardMuted = null;
    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        loadCoding();

        this.register();
        this.stringUtils = new StringsUtils(getInstance());
        this.blackListFile = new BlackListFile(getInstance());
        this.muteFile = new MuteFile(getInstance());
        this.fileTypeFanction = new fileTypeFanction(getInstance());

        getBlackListFile().create();
        getMuteFile().create(fileType.mainFile);
        getMuteFile().create(fileType.muteFile);

        Bukkit.getPluginManager().registerEvents(new event(this), this);
        getCommand("banned").setExecutor(new BannedCommand(this));
        Bukkit.getConsoleSender().sendMessage(color("&e[ Sender message ] has been enabled"));
    }

    @Override
    public void onDisable() {
        jda.shutdown();
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(color("&a[ Sender message ] has been disabled"));
    }

    @Override
    public void onLoad() {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(getConfig().getString("discord-bot-token"))
                    .setActivity(Activity.playing(getDescription().getName() + " v" + getDescription().getVersion()))
                    .addEventListeners(new DiscordCore()).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        super.onLoad();
    }

    private void register() {
        PluginManager plugin = Bukkit.getPluginManager();

        getCommand("blacklist_word").setExecutor(new BlacklistCommands(this));
        getCommand("mute").setExecutor(new MuteCommands(this));

        plugin.registerEvents(new ChatEvent<>(this), this);
    }
    public JDA getJda() {
        return jda;
    }

    public static long getChannelid() {
        return channelid;
    }

    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> color(List<String> messages){
        return messages.stream().map(core::color).collect(Collectors.toList());
    }

    private void loadCoding(){
        saveDefaultConfig();
    }

    public static void sendMessageToMinecraft(Message message) {

        final Role[] role = {null};
        message.getMember().getRoles().forEach(r -> {
            if (!r.getName().contains("-") && role[0] == null) {
                role[0] = r;
            }
        });

        if (message.getAttachments().size() > 0) {
            Bukkit.broadcastMessage(color("&9&l[Discord] " + role[0].getName() + " | " + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator()  + " &9&l|&f " + message.getAttachments().get(0).getUrl()));
        }
        if (message.getAttachments().size() <=0) {
            Bukkit.broadcastMessage(color("&9&l[Discord] " + role[0].getName() + " | &e" + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator()  + " &9&l|&f " + message.getContentRaw()));
        }
    }

    public void sendMessageToDiscord(Player player, String message, Webhook webhook) {

        if( webhook == null) {
            System.out.println("No Webhook :(");
            return;
        }

        if (getBlackListFile().getConfiguration().getStringList("Users").contains(player.getName())) {
            return;
        }

        if (getBlackListFile().getConfiguration().getStringList("BlackList.words").contains(message)){
            return;
        }

        PermissionUser user = PermissionsEx.getUser(player.getName());
        String rank = user.getParentIdentifiers().get(0);

        DateFormat formattedDate = new SimpleDateFormat("yyyy - MMM dd | HH:mm:ss");
        Date date = new Date();
        String currentData = formattedDate.format(date);

        // System.out.println("Webhook: " + webhook.getName()+"\nWebhook Chanel: " + webhook.getChannel().getName());

        webhook.getChannel().sendMessage("> **" + rank + " | " + player.getName() + ":** " + message + "  ``( " + currentData + " )``").queue();
    }

    public StringsUtils getStringUtils() {
        return stringUtils;
    }

    public BlackListFile getBlackListFile() {
        return blackListFile;
    }

    public core getInstance() {
        return instance;
    }

    public MuteFile getMuteFile() {
        return muteFile;
    }

    public system.MinecraftCommand.FilesCreator.fileTypeFanction getFileTypeFanction() {
        return fileTypeFanction;
    }

}

