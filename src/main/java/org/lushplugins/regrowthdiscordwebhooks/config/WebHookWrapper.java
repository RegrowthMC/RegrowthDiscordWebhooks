package org.lushplugins.regrowthdiscordwebhooks.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github._4drian3d.jdwebhooks.webhook.WebHookClient;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public record WebHookWrapper(
    String webHookUrl,
    MessageWrapper message
) {
    private static final Cache<String, WebHookClient> CLIENTS_CACHE = CacheBuilder.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    public WebHookClient client() {
        try {
            return CLIENTS_CACHE.get(this.webHookUrl, () -> WebHookClient.fromURL(this.webHookUrl));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<HttpResponse<String>> send() {
        return this.client().executeWebHook(this.message.prepareExecution());
    }
}
