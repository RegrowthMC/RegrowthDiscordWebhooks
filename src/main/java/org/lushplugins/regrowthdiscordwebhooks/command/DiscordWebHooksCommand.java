package org.lushplugins.regrowthdiscordwebhooks.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.lushplugins.regrowthdiscordwebhooks.RegrowthDiscordWebHooks;
import org.lushplugins.regrowthdiscordwebhooks.config.WebHookWrapper;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
@Command({"discordwebhook", "discordwebhooks"})
public class DiscordWebHooksCommand {
    private static final Pattern PLACEHOLDERS_PATTERN = Pattern.compile("(%\\w+%)=(\\w+),?");

    @Subcommand("send")
    @CommandPermission("regrowthdiscordwebhooks.send")
    public void send(CommandSender sender, WebHookWrapper webHook, @Optional @Flag("placeholders") String rawPlaceholders) {
        sender.sendMessage(Component.text()
            .content("Sending '%s' webhook...".formatted(webHook.id()))
            .color(TextColor.fromHexString("#ffda54"))
            .build());

        Map<String, String> replacements = readReplacements(rawPlaceholders);
        webHook.send(replacements).thenAccept(response -> {
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

    public Map<String, String> readReplacements(String rawPlaceholders) {
        Map<String, String> replacements = new HashMap<>();

        Matcher matcher = PLACEHOLDERS_PATTERN.matcher(rawPlaceholders);
        while(matcher.find()) {
            String placeholder = matcher.group(1);
            String replacement = matcher.group(2);
            replacements.put(placeholder, replacement);
        }

        return replacements;
    }
}
