package org.lushplugins.regrowthdiscordwebhooks.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.lushplugins.regrowthdiscordwebhooks.RegrowthDiscordWebHooks;
import org.lushplugins.regrowthdiscordwebhooks.config.WebHookWrapper;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@SuppressWarnings("unused")
@Command({"discordwebhook", "discordwebhooks"})
public class DiscordWebHooksCommand {

    @Subcommand("send")
    @CommandPermission("regrowthdiscordwebhooks.send")
    public void send(CommandSender sender, WebHookWrapper webHook) {
        sender.sendMessage(Component.text()
            .content("Sending '%s' webhook...".formatted(webHook.id()))
            .color(TextColor.fromHexString("#ffda54"))
            .build());

        webHook.send().thenAccept(response -> {
            sender.sendMessage(Component.text()
                .content("Successfully sent '%s' webhook!".formatted(webHook.id()))
                .color(TextColor.fromHexString("#b7faa2"))
                .build());
        });
    }

    @Subcommand("reload")
    @CommandPermission("regrowthdiscordwebhooks.reload")
    public void reload(CommandSender sender) {
        RegrowthDiscordWebHooks.getInstance().getMessageManager().reload();

        sender.sendMessage(Component.text()
            .content("Successfully reloaded RegrowthDiscordWebHooks!")
            .color(TextColor.fromHexString("#b7faa2"))
            .build());
    }
}
