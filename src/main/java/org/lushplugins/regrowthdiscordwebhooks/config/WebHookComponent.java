package org.lushplugins.regrowthdiscordwebhooks.config;

import io.github._4drian3d.jdwebhooks.component.*;
import io.github._4drian3d.jdwebhooks.media.FileAttachment;
import io.github._4drian3d.jdwebhooks.media.URLMediaReference;
import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.lushlib.utils.YamlUtils;
import org.lushplugins.regrowthdiscordwebhooks.RegrowthDiscordWebHooks;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class WebHookComponent {

    private static TextDisplayComponent readTextDisplay(ConfigurationSection config) {
        String content = config.getString("content", "");
        return Component.textDisplay(content);
    }

    private static AccessoryComponent readAccessory(ConfigurationSection config) {
        String url = config.getString("url");
        String description = config.getString("description");
        boolean spoiler = config.getBoolean("spoiler");

        return Component.thumbnail()
            .media(URLMediaReference.from(url))
            .description(description)
            .spoiler(spoiler)
            .build();
    }

    public static Component read(ConfigurationSection config) {
        return switch(config.getString("type")) {
            case "text" -> readTextDisplay(config);
            case "section" -> {
                List<TextDisplayComponent> components = config.getStringList("components").stream()
                    .map(Component::textDisplay)
                    .toList();
                AccessoryComponent accessory = readAccessory(config.getConfigurationSection("accessory"));

                yield Component.section()
                    .components(components)
                    .accessory(accessory)
                    .build();
            }
            case "media-gallery" -> {
                List<MediaGalleryComponent.Item> items = YamlUtils.getConfigurationSections(config, "media").stream()
                    .map(section -> {
                        String url = section.getString("url");
                        String description = section.getString("description");
                        boolean spoiler = section.getBoolean("spoiler");

                        return MediaGalleryComponent.itemBuilder()
                            .media(URLMediaReference.from(url))
                            .description(description)
                            .spoiler(spoiler)
                            .build();
                    })
                    .toList();

                yield Component.mediaGallery()
                    .items(items)
                    .build();
            }
            case "file" -> {
                Path file = RegrowthDiscordWebHooks.getInstance().getDataPath()
                    .resolve("attachments")
                    .resolve(config.getString("file"));
                boolean spoiler = config.getBoolean("spoiler");

                yield Component.file()
                    .file(file)
                    .spoiler(spoiler)
                    .build();
            }
            case "separator" -> {
                boolean divider = config.getBoolean("divider");
                SeparatorComponent.Spacing spacing = switch (config.getString("spacing")) {
                    case "small" -> SeparatorComponent.Spacing.SMALL;
                    case null, default -> SeparatorComponent.Spacing.LARGE;
                };

                yield Component.separator()
                    .divider(divider)
                    .spacing(spacing)
                    .build();
            }
            case "container", "embed" -> {
                List<ContainerableComponent> components = YamlUtils.getConfigurationSections(config, "components").stream()
                    .map(section -> read(section) instanceof ContainerableComponent component ? component : null)
                    .filter(Objects::nonNull)
                    .toList();
                int accentColor;
                if (config.isString("accent-color")) {
                    String hex = config.getString("accent-color", "#000000")
                        .replace("#", "");
                    accentColor = Integer.parseInt(hex, 16);
                } else {
                    accentColor = config.getInt("accent-color");
                }
                boolean spoiler = config.getBoolean("spoiler");

                yield Component.container()
                    .components(components)
                    .accentColor(accentColor)
                    .spoiler(spoiler)
                    .build();
            }
            case null, default -> readTextDisplay(config);
        };
    }
}
