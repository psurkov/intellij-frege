package com.plugin.frege.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiReference
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.parentOfTypes
import com.plugin.frege.psi.*
import com.plugin.frege.psi.impl.FregeCompositeElementImpl
import com.plugin.frege.resolve.FregeReferenceBase
import com.plugin.frege.resolve.FregeResolveUtil.findMethodsFromUsage
import com.plugin.frege.resolve.FregeResolveUtil.resolveBindingByNameElement

open class FregeSymbolOperatorMixin(node: ASTNode) :
    FregeCompositeElementImpl(node), FregeResolvableElement, PsiIdentifier {

    override fun getReference(): PsiReference {
        return object : FregeReferenceBase(this, TextRange(0, textLength)) {
            override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
                return when {
                    psiElement.parentOfTypes(FregeAnnotationItem::class, FregeFunctionLhs::class) != null -> {
                        resolveBindingByNameElement(psiElement, incompleteCode)
                    }
                    else -> {
                        val namedElementOwner = namedElementOwner
                        if (namedElementOwner != null) {
                            return listOf(namedElementOwner)
                        }
                        findMethodsFromUsage(psiElement, incompleteCode)
                    }
                }
            }

            override fun handleElementRename(name: String): PsiElement {
                return psiElement.replace(FregeElementFactory.createSymbolOperator(psiElement.project, name))
            }
        }
    }

    override fun getTokenType(): IElementType {
        return FregeTypes.SYMBOL_OPERATOR
    }
}
