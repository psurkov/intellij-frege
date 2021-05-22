package com.plugin.frege.resolve

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.plugin.frege.psi.FregeBinding
import com.plugin.frege.psi.FregeElementFactory.createVarId
import com.plugin.frege.psi.FregeParam
import com.plugin.frege.psi.impl.FregePsiClassUtilImpl.getAllMethodsByImportName
import com.plugin.frege.psi.impl.FregePsiClassUtilImpl.getMethodsByQualifiedName
import com.plugin.frege.psi.impl.FregePsiUtilImpl.findClassesInCurrentFile
import com.plugin.frege.psi.impl.FregePsiUtilImpl.findElementsWithinElement
import com.plugin.frege.psi.impl.FregePsiUtilImpl.findElementsWithinScope
import com.plugin.frege.psi.impl.FregePsiUtilImpl.findImportsNamesForElement
import com.plugin.frege.psi.impl.FregePsiUtilImpl.findWhereInExpression
import com.plugin.frege.psi.impl.FregePsiUtilImpl.getByTypePredicateCheckingName
import com.plugin.frege.psi.impl.FregePsiUtilImpl.getParentBinding
import com.plugin.frege.psi.impl.FregePsiUtilImpl.mergeQualifiedNames
import com.plugin.frege.psi.impl.FregePsiUtilImpl.scopeOfElement

class FregeQVaridReference(element: PsiElement) : FregeReferenceBase(element, TextRange(0, element.textLength)) {

    override fun handleElementRename(name: String): PsiElement {
        return psiElement.replace(createVarId(psiElement.project, name))
    }

    // TODO take into account: qualified names
    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
        val result = tryFindFunction(incompleteCode).toMutableList()
        if (result.isNotEmpty() && !incompleteCode) {
            return result
        }

        result.addAll(tryFindParameters(incompleteCode))
        if (result.isNotEmpty() && !incompleteCode) {
            return result
        }

        result.addAll(tryFindMethodsInClassesInCurrentFile())
        if (result.isNotEmpty() && !incompleteCode) {
            return result
        }

        result.addAll(tryFindMethodsByImports(incompleteCode))
        return result
    }

    private fun tryFindFunction(incompleteCode: Boolean): List<PsiElement> {
        val predicate = getByTypePredicateCheckingName(FregeBinding::class, psiElement.text, incompleteCode)

        // check if this expression has `where` ans search there for definitions if it does.
        val where = findWhereInExpression(psiElement)
        if (where?.indentSection != null) {
            val whereFuncNames = findElementsWithinScope(where.indentSection!!, predicate)
            if (whereFuncNames.isNotEmpty()) {
                return whereFuncNames
            }
        }

        // search for definitions in the current and outer scopes
        var scope: PsiElement? = scopeOfElement(psiElement)
        while (scope != null) {
            val functionNames = findElementsWithinScope(scope, predicate)
            if (functionNames.isNotEmpty()) {
                return listOf(functionNames.minByOrNull { it.textOffset }!!)
            }
            scope = scopeOfElement(scope.parent)
        }
        return emptyList()
    }

    private fun tryFindParameters(incompleteCode: Boolean): List<PsiElement> { // TODO copy/paste
        val predicate = getByTypePredicateCheckingName(FregeParam::class, psiElement.text, incompleteCode)
        val where = findWhereInExpression(psiElement)
        if (where != null) {
            val params = findElementsWithinScope(where, predicate)
            if (params.isNotEmpty()) {
                return params
            }
        }

        var binding = getParentBinding(psiElement)
        while (binding != null) {
            val params = findElementsWithinElement(binding, predicate)
            if (params.isNotEmpty()) {
                return params
            }
            binding = getParentBinding(binding.parent)
        }
        return emptyList()
    }

    private fun tryFindMethodsByImports(incompleteCode: Boolean): List<PsiElement> {
        val methodName = psiElement.text
        val project = psiElement.project
        val imports = findImportsNamesForElement(psiElement, true)
        val result: MutableList<PsiElement> = ArrayList()
        for (currentImport in imports) {
            if (incompleteCode) {
                result.addAll(getAllMethodsByImportName(project, currentImport))
            } else {
                val qualifiedName = mergeQualifiedNames(currentImport, methodName)
                result.addAll(getMethodsByQualifiedName(project, qualifiedName))
                if (result.isNotEmpty()) {
                    break
                }
            }
        }
        return result
    }

    private fun tryFindMethodsInClassesInCurrentFile(): List<PsiMethod> { // TODO incomplete code
        val methodName = psiElement.text
        val project = psiElement.project
        val availableClasses = findClassesInCurrentFile(psiElement)
        for (clazz in availableClasses) {
            val className = clazz.qualifiedName ?: continue
            val qualifiedName = mergeQualifiedNames(className, methodName)
            val methods = getMethodsByQualifiedName(project, qualifiedName)
            if (methods.isNotEmpty()) {
                return methods // TODO errors if several references in different classes
            }
        }

        return emptyList()
    }
}
