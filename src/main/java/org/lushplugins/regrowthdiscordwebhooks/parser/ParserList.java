package org.lushplugins.regrowthdiscordwebhooks.parser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ParserList {
    private final List<Parser> parsers = new ArrayList<>();

    public @NotNull String parse(@NotNull String string) {
        for (Parser parser : this.parsers) {
            string = parser.parse(string);
        }
        return string;
    }

    public void add(Parser parser) {
        this.parsers.add(parser);
    }
}
