package org.lushplugins.regrowthdiscordwebhooks;

import org.lushplugins.lushlib.plugin.SpigotPlugin;
import org.lushplugins.regrowthdiscordwebhooks.command.DiscordWebHooksCommand;
import org.lushplugins.regrowthdiscordwebhooks.config.MessageManager;
import revxrsal.commands.bukkit.BukkitLamp;

public final class RegrowthDiscordWebHooks extends SpigotPlugin {
    private static RegrowthDiscordWebHooks plugin;

    private MessageManager messageManager;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        this.messageManager = new MessageManager();
        this.messageManager.reload();

        BukkitLamp.builder(this)
            .build()
            .register(new DiscordWebHooksCommand());
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public static RegrowthDiscordWebHooks getInstance() {
        return plugin;
    }
}
