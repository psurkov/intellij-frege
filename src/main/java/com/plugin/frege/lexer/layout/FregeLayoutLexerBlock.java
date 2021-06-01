package com.plugin.frege.lexer.layout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FregeLayoutLexerBlock {
    private final @NotNull List<@NotNull FregeLayoutLexerToken> virtualPrefixTokens = new ArrayList<>();
    private final @NotNull List<@NotNull FregeLayoutLexerToken> mainTokens = new ArrayList<>();
    private boolean containsCode = false;
    private boolean containsLet = false;

    public void add(@NotNull FregeLayoutLexerToken token) {
        updateContains(token);
        mainTokens.add(token);
    }

    public void addToVirtualPrefix(@NotNull FregeLayoutLexerToken token) {
        if (!token.isVirtual()) {
            throw new FregeLayoutLexerException(
                    new IllegalArgumentException(token.type + "is not a virtual type"));
        }
        updateContains(token);
        virtualPrefixTokens.add(token);
    }

    private void updateContains(@NotNull FregeLayoutLexerToken token) {
        if (token.isCode()) {
            containsCode = true;
        }
        if (token.isLet()) {
            containsLet = true;
        }
    }

    public int size() {
        return virtualPrefixTokens.size() + mainTokens.size();
    }

    public boolean isMainTokensEmpty() {
        return mainTokens.isEmpty();
    }

    public FregeLayoutLexerToken get(int index) {
        Objects.checkIndex(index, size());
        if (index < virtualPrefixTokens.size()) {
            return virtualPrefixTokens.get(index);
        } else {
            return mainTokens.get(index - virtualPrefixTokens.size());
        }
    }

    public boolean isContainsCode() {
        return containsCode;
    }

    public boolean isContainsLet() {
        return containsLet;
    }
}