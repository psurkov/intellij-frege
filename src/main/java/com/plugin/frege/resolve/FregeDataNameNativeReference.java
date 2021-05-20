package com.plugin.frege.resolve;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FregeDataNameNativeReference extends FregeReferenceBase {
    public FregeDataNameNativeReference(@NotNull PsiElement element) {
        super(element, element.getTextRange());
    }

    @Override
    protected @NotNull List<PsiElement> resolveInner(boolean incompleteCode) {
        return List.of(psiElement);
    }
}