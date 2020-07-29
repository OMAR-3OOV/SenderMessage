package system.MinecraftCommand;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import system.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlackListFile {

    private final core plugin;

    private final File file;
    private YamlConfiguration configuration;

    public BlackListFile(core core) {
        this.plugin = core;
        this.file = new File("plugins//SenderMessage//BlacklistWord.yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public File getFile(){
        return file;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public void create() {
        if (!(file.exists())) {
            try{
                file.createNewFile();
                List<String> list = new ArrayList<>();
                List<String> Users = new ArrayList<>();

                configuration.set("BlackList.available", true);
                configuration.set("BlackList.words", list);
                configuration.set("Users", Users);
            }catch (IOException ex) {
                ex.printStackTrace();
            }

            try {
                configuration.save(file);
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void getString(String path) {
        getConfiguration().getString(path);
    }

    public List<String> getStringList(String path) {
        return getConfiguration().getStringList(path);
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public boolean checkPlayerMuted(String player) {
        return getStringList("BlackList.Users").contains(player);
    }

    public boolean checkUserInList(UUID uuid) {
        return getConfiguration().getConfigurationSection("Users").contains(uuid.toString());
    }

    public boolean checkUserInList(OfflinePlayer offlinePlayer) {
        return getConfiguration().getConfigurationSection("Users").contains(offlinePlayer.getName());
    }
}
