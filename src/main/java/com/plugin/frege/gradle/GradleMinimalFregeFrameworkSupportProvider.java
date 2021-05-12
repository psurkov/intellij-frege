package com.plugin.frege.gradle;

import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import com.intellij.openapi.externalSystem.model.project.ProjectId;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.plugin.frege.FregeIcons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.frameworkSupport.BuildScriptDataBuilder;
import org.jetbrains.plugins.gradle.frameworkSupport.GradleFrameworkSupportProvider;

import javax.swing.*;
import java.io.IOException;

public class GradleMinimalFregeFrameworkSupportProvider extends GradleFrameworkSupportProvider {
    public static final String ID = "Frege Minimal";
    private final GradleMinimalFregeForm settingsForm;

    public GradleMinimalFregeFrameworkSupportProvider() {
        this.settingsForm = new GradleMinimalFregeForm();
    }

    @Override
    public @NotNull FrameworkTypeEx getFrameworkType() {
        return new FrameworkTypeEx(ID) {
            @Override
            public @NotNull FrameworkSupportInModuleProvider createProvider() {
                return GradleMinimalFregeFrameworkSupportProvider.this;
            }

            @Override
            public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getPresentableName() {
                return "Frege Minimal";
            }

            @Override
            public @NotNull Icon getIcon() {
                return FregeIcons.FILE;
            }
        };
    }

    @Override
    public JComponent createComponent() {
        return settingsForm.getPanel();
    }


    @Override
    public void addSupport(@NotNull ProjectId projectId,
                           @NotNull Module module,
                           @NotNull ModifiableRootModel rootModel,
                           @NotNull ModifiableModelsProvider modifiableModelsProvider,
                           @NotNull BuildScriptDataBuilder buildScriptData) {
        try {
            GradleFregeScriptBuilder gradleFregeScriptBuilder = new GradleFregeScriptBuilder(settingsForm);
            byte[] contentBytes = gradleFregeScriptBuilder.build();

            buildScriptData.getBuildScriptFile().setBinaryContent(contentBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
