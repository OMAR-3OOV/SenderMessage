package system.Utils;

import system.core;

public final class StringsUtils {

    private final core plugin;

    public StringsUtils(core core){
        this.plugin = core;
    }

    public String getPrefix() {
        return plugin.getConfig().getString("prefix");
    }

    public String getNoPermission() {
        return plugin.getConfig().getString("no-permission");
    }
}

