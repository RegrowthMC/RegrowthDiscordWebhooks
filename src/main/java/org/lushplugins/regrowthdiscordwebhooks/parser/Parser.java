package org.lushplugins.regrowthdiscordwebhooks.parser;

import org.jetbrains.annotations.NotNull;

public interface Parser {
    @NotNull String parse(String string);
}
