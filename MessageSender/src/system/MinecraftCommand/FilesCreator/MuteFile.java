package system.MinecraftCommand.FilesCreator;

import org.bukkit.configuration.file.YamlConfiguration;
import system.core;

import java.io.File;

public class MuteFile<getMainFile> {

    private final core plugin;

    private final File file;
    private final File muteFile;
    private YamlConfiguration configuration;

    public MuteFile(core core) {
        this.plugin = core;
        this.file = new File("plugins//SenderMessage//Mute");
        this.muteFile = new File("plugins//SenderMessage//Mute//mutelog.yml");
        this.configuration = YamlConfiguration.loadConfiguration(muteFile);
    }

    public File getMainFile() {
        return this.file;
    }

    public File getMuteFile() {
        return this.muteFile;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public void create(fileType type) {
        plugin.getInstance().getFileTypeFanction().create(type);
    }

    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(getMainFile());
    }
}
