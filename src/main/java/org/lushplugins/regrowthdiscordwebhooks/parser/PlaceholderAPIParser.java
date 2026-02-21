package org.lushplugins.regrowthdiscordwebhooks.parser;

import me.clip.placeholderapi.PlaceholderAPI;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIParser implements Parser {

    @Override
    public @NotNull String parse(String string) {
        return PlaceholderAPI.setPlaceholders(null, string);
    }
}
