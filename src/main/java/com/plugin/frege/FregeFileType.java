package com.plugin.frege;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FregeFileType extends LanguageFileType {

    public static final FregeFileType INSTANCE = new FregeFileType();

    private FregeFileType() {
        super(FregeLanguage.INSTANCE);
    }

    @Override
    public @NonNls
    @NotNull String getName() {
        return "Frege File";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "Frege language file";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return "fr";
    }

    @Override
    public @Nullable Icon getIcon() {
        return FregeIcons.FILE;
    }
}
