package org.lushplugins.regrowthdiscordwebhooks.config;

import io.github._4drian3d.jdwebhooks.component.Component;
import io.github._4drian3d.jdwebhooks.media.FileAttachment;
import io.github._4drian3d.jdwebhooks.webhook.WebHookExecution;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lushplugins.lushlib.utils.YamlUtils;
import org.lushplugins.regrowthdiscordwebhooks.RegrowthDiscordWebHooks;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageManager {
    private Map<String, WebHookWrapper> messages;

    public MessageManager() {
        if (!new File(RegrowthDiscordWebHooks.getInstance().getDataFolder(), "messages").exists()) {
            RegrowthDiscordWebHooks.getInstance().saveDefaultResource("messages/example.yml");
            RegrowthDiscordWebHooks.getInstance().saveDefaultResource("attachments/example.txt");
        }
    }

    public void reload() {
        this.messages = readMessagesFilesInDirectory(RegrowthDiscordWebHooks.getInstance().getDataPath().resolve("messages"));
    }

    public Map<String, WebHookWrapper> readMessagesConfigFile(YamlConfiguration config) {
        String defaultWebHookUrl = config.getString("webhook.webhook-url");
        String defaultUsername = config.getString("webhook.username");
        String defaultAvatarUrl = config.getString("webhook.avatar-url");

        return YamlUtils.getConfigurationSections(config, "messages").stream()
            .collect(Collectors.toMap(
                ConfigurationSection::getName,
                section -> {
                    String webhookUrl = section.getString("webhook-url", defaultWebHookUrl);
                    String username = section.getString("username", defaultUsername);
                    String avatarUrl = section.getString("avatar-url", defaultAvatarUrl);
                    Boolean tts = section.isBoolean("tts") ? section.getBoolean("tts") : null;
                    boolean suppressNotifications = section.getBoolean("suppress-notifs");
                    List<Component> components = YamlUtils.getConfigurationSections(section, "components").stream()
                        .map(WebHookComponent::read)
                        .toList();
                    List<FileAttachment> attachments = section.getStringList("attachments").stream()
                        .map(path -> RegrowthDiscordWebHooks.getInstance().getDataPath()
                            .resolve("attachments")
                            .resolve(path))
                        .map(FileAttachment::fromFile)
                        .toList();

                    return new WebHookWrapper(
                        webhookUrl,
                        WebHookExecution.builder()
                            .username(username)
                            .avatarURL(avatarUrl)
                            .tts(tts)
                            .suppressNotifications(suppressNotifications)
                            .components(components)
                            .fileAttachments(attachments)
                            .build()
                    );
                }
            ));
    }

    public Map<String, WebHookWrapper> readMessagesFilesInDirectory(Path directoryPath) {
        return YamlUtils.readConfigsInDirectory(directoryPath).stream()
            .map(file -> readMessagesConfigFile(file.second()))
            .reduce(new HashMap<>(), (map, toMerge) -> {
                map.putAll(toMerge);
                return map;
            });
    }

    public WebHookWrapper getMessage(String id) {
        return messages.get(id);
    }
}
