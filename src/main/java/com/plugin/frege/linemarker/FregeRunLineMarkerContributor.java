package com.plugin.frege.linemarker;

import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.psi.PsiElement;
import com.plugin.frege.psi.FregeFunctionName;
import com.plugin.frege.psi.impl.FregeFunctionNameImpl;
import com.plugin.frege.psi.impl.FregePsiUtilImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FregeRunLineMarkerContributor extends RunLineMarkerContributor {
    @Override
    public @Nullable Info getInfo(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (!(parent instanceof FregeFunctionName)) {
            return null;
        }
        FregeFunctionNameImpl functionName = (FregeFunctionNameImpl) parent;
        if (!functionName.isMainFunctionBinding()) {
            return null;
        }

        final String moduleName = FregePsiUtilImpl.getModuleName(element);
        if (moduleName == null) return null;

        return new Info(AllIcons.RunConfigurations.TestState.Run,
                new AnAction[]{
                        new FregeRunAction(moduleName)},
                (PsiElement t) -> "Run " + moduleName);
    }
}
