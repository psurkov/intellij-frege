package com.plugin.frege.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFinder;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FregePsiElementFinder extends PsiElementFinder {
    @Override
    public @Nullable PsiClass findClass(@NotNull String qualifiedName, @NotNull GlobalSearchScope scope) {
        PsiClass[] classes = findClasses(qualifiedName, scope);
        if (classes.length == 0) {
            return null;
        }

        return classes[0];
    }

    @Override
    public PsiClass @NotNull [] findClasses(@NotNull String qualifiedName, @NotNull GlobalSearchScope scope) {
        Project project = scope.getProject();
        if (project == null) {
            return PsiClass.EMPTY_ARRAY;
        }

        PsiManager manager = PsiManager.getInstance(project);
        List<PsiClass> classes = new ArrayList<>();

        ProjectFileIndex.SERVICE.getInstance(project).iterateContent(fileOrDir -> {
            if (fileOrDir.isDirectory()) {
                return true;
            }

            PsiFile file = manager.findFile(fileOrDir);
            if (!(file instanceof FregeFile)) {
                return true;
            }

            classes.addAll(PsiTreeUtil.findChildrenOfType(file, FregePsiClass.class).stream()
                    .filter(clazz -> Objects.equals(clazz.getQualifiedName(), qualifiedName))
                    .collect(Collectors.toList()));

            return !classes.isEmpty();
        });

        return classes.toArray(PsiClass[]::new);
    }
}