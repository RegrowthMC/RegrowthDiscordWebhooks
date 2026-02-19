package org.lushplugins.regrowthdiscordwebhooks;

import org.bukkit.plugin.java.JavaPlugin;

public final class RegrowthDiscordWebhooks extends JavaPlugin {
    private static RegrowthDiscordWebhooks plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Enable implementation
    }

    @Override
    public void onDisable() {
        // Disable implementation
    }

    public static RegrowthDiscordWebhooks getInstance() {
        return plugin;
    }
}
