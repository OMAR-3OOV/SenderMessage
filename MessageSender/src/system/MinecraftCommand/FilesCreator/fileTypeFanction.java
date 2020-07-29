package system.MinecraftCommand.FilesCreator;

import org.bukkit.entity.Player;
import system.core;

import java.io.IOException;
import java.util.ArrayList;

public class fileTypeFanction {

    private final core plugin;

    public fileTypeFanction(core core) {
        this.plugin = core;
    }

    public void create(fileType type) {
        if (type == fileType.mainFile) {
            if (!plugin.getMuteFile().getMainFile().exists()) {
                plugin.getMuteFile().getMainFile().mkdirs();
            }
        }
        if (type == fileType.muteFile) {
            if (!plugin.getMuteFile().getMuteFile().exists()) {
                try {
                    plugin.getMuteFile().getMuteFile().createNewFile();

                    ArrayList<Player> users = new ArrayList<>();

                    plugin.getMuteFile().getConfiguration().set("Users", users);
                }catch (IOException ex) {
                    ex.printStackTrace();
                }

                try {
                    plugin.getMuteFile().getConfiguration().save(type.getFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                return;
            }
        }
    }
}
