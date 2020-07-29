package system.MinecraftCommand.FilesCreator;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum fileType {

    mainFile(new File("plugins//SenderMessage//Mute"), null, "plugins//SenderMessage//Mute",0),
    muteFile(new File("plugins//SenderMessage//Mute//mutelog.yml"), YamlConfiguration.loadConfiguration(new File("plugins//SenderMessage//Mute/mutelog.yml")), "plugins//SenderMessage//Mute//mutelog.yml", 1);

    private final String path;
    private final int id;
    private final File file;
    private final YamlConfiguration configuration;

    fileType(File file, YamlConfiguration configuration, String path, int id) {
       this.path = path;
       this.id = id;
       this.file = file;
       this.configuration = configuration;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }
}
