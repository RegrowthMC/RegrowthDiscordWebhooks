package org.lushplugins.regrowthdiscordwebhooks.config;

import io.github._4drian3d.jdwebhooks.component.Component;
import io.github._4drian3d.jdwebhooks.component.TextDisplayComponent;
import io.github._4drian3d.jdwebhooks.media.FileAttachment;
import io.github._4drian3d.jdwebhooks.webhook.WebHookExecution;
import org.lushplugins.regrowthdiscordwebhooks.RegrowthDiscordWebHooks;
import org.lushplugins.regrowthdiscordwebhooks.parser.ParserList;

import java.util.List;

public record MessageWrapper(
    String username,
    String avatarUrl,
    Boolean tts,
    boolean suppressNotifications,
    List<Component> components,
    List<FileAttachment> attachments
) {

    public WebHookExecution prepareExecution() {
        ParserList parsers = RegrowthDiscordWebHooks.getInstance().getParsers();
        List<Component> parsedComponents = this.components.stream()
            .map(component -> {
                if (component instanceof TextDisplayComponent textComponent) {
                    return Component.textDisplay(parsers.parse(textComponent.content()));
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
}
