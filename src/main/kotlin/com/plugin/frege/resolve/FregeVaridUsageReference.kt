package com.plugin.frege.resolve

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.plugin.frege.psi.FregeDoDecl
import com.plugin.frege.psi.FregeElementFactory.createVarId
import com.plugin.frege.psi.FregeParameter
import com.plugin.frege.psi.impl.FregePsiUtilImpl.findElementsWithinElement
import com.plugin.frege.psi.impl.FregePsiUtilImpl.findElementsWithinScope
import com.plugin.frege.psi.impl.FregePsiUtilImpl.findWhereInExpression
import com.plugin.frege.psi.impl.FregePsiUtilImpl.getByTypePredicateCheckingName
import com.plugin.frege.psi.impl.FregePsiUtilImpl.getParentBinding
import com.plugin.frege.psi.impl.FregePsiUtilImpl.scopeOfElement

class FregeVaridUsageReference(element: PsiElement) : FregeReferenceBase(element, TextRange(0, element.textLength)) {

    override fun handleElementRename(name: String): PsiElement {
        return psiElement.replace(createVarId(psiElement.project, name))
    }

    // TODO take into account: qualified names
    override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
        val result = findParameters(incompleteCode).toMutableList()
        if (result.isNotEmpty() && !incompleteCode) {
            return result
        }
        result.addAll(FregeResolveUtil.findMethodsFromUsage(psiElement, incompleteCode))
        return result
    }

    private fun findParameters(incompleteCode: Boolean): List<PsiElement> { // TODO copy/paste from resolve util
        val predicate = getByTypePredicateCheckingName(FregeParameter::class, psiElement.text, incompleteCode)
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

        return findParametersInDoDecls(incompleteCode)
    }

    private fun findParametersInDoDecls(incompleteCode: Boolean): List<PsiElement> {
        val predicate = getByTypePredicateCheckingName(FregeParameter::class, psiElement.text, incompleteCode)
        var scope = scopeOfElement(psiElement)
        while (scope != null) {
            val doDecl = PsiTreeUtil.getPrevSiblingOfType(scope, FregeDoDecl::class.java)
            if (doDecl?.pattern != null) {
                val params = findElementsWithinElement(doDecl.pattern, predicate)
                if (params.isNotEmpty()) {
                    return params
                }
            }

            scope = scopeOfElement(scope.parent)
        }

        return emptyList()
    }
}
