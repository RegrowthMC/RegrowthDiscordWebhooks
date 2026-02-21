package org.lushplugins.regrowthdiscordwebhooks;

import org.lushplugins.lushlib.plugin.SpigotPlugin;
import org.lushplugins.regrowthdiscordwebhooks.command.DiscordWebHooksCommand;
import org.lushplugins.regrowthdiscordwebhooks.config.MessageManager;
import org.lushplugins.regrowthdiscordwebhooks.parser.ParserList;
import org.lushplugins.regrowthdiscordwebhooks.parser.PlaceholderAPIParser;
import revxrsal.commands.bukkit.BukkitLamp;

public final class RegrowthDiscordWebHooks extends SpigotPlugin {
    private static RegrowthDiscordWebHooks plugin;

    private ParserList parsers;
    private MessageManager messageManager;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        this.parsers = new ParserList();
        this.messageManager = new MessageManager();
        this.messageManager.reload();

        ifPluginPresent("PlaceholderAPI", () -> this.parsers.add(new PlaceholderAPIParser()));

        BukkitLamp.builder(this)
            .build()
            .register(new DiscordWebHooksCommand());
    }

    public ParserList getParsers() {
        return parsers;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public static RegrowthDiscordWebHooks getInstance() {
        return plugin;
    }
}
