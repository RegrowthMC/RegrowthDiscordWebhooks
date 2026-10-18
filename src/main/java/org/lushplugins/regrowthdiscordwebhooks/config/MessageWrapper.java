package org.lushplugins.regrowthdiscordwebhooks.config;

import io.github._4drian3d.jdwebhooks.component.Component;
import io.github._4drian3d.jdwebhooks.component.ContainerComponent;
import io.github._4drian3d.jdwebhooks.component.TextDisplayComponent;
import io.github._4drian3d.jdwebhooks.media.FileAttachment;
import io.github._4drian3d.jdwebhooks.webhook.WebHookExecution;
import org.lushplugins.regrowthdiscordwebhooks.RegrowthDiscordWebHooks;
import org.lushplugins.regrowthdiscordwebhooks.parser.ParserList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record MessageWrapper(
    String username,
    String avatarUrl,
    Boolean tts,
    boolean suppressNotifications,
    List<Component> components,
    List<FileAttachment> attachments
) {

    public WebHookExecution prepareExecution(Map<String, String> replacements) {
        ParserList parsers = RegrowthDiscordWebHooks.getInstance().getParsers();
        List<Component> parsedComponents = this.components.stream()
            .map(component -> {
                if (component instanceof TextDisplayComponent textComponent) {
                    return Component.textDisplay(parse(textComponent.content(), parsers, replacements));
                } else if (component instanceof ContainerComponent container) {
                    return Component.container()
                        .components(container.components().stream()
                            .map(containerComponent -> {
                                if (containerComponent instanceof TextDisplayComponent containerTextComponent) {
                                    return Component.textDisplay(parse(containerTextComponent.content(), parsers, replacements));
                                } else {
                                    return containerComponent;
                                }
                            })
                            .toList())
                        .accentColor(container.accentColor())
                        .spoiler(container.spoiler())
                        .build();
                } else {
                    return component;
                }
            })
            .toList();

        return WebHookExecution.builder()
            .username(parsers.parse(this.username))
            .avatarURL(this.avatarUrl)
            .tts(this.tts)
            .suppressNotifications(this.suppressNotifications)
            .components(parsedComponents)
            .fileAttachments(this.attachments)
            .build();
    }

    public WebHookExecution prepareExecution() {
        return prepareExecution(Collections.emptyMap());
    }

    private String parse(String string, ParserList parsers, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String key = entry.getKey();
            String replacement = entry.getValue();
            string = string.replace(key, replacement);
        }

        return parsers.parse(string);
    }
}
