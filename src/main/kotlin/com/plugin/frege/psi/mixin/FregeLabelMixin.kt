package com.plugin.frege.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiModifierList
import com.intellij.psi.impl.light.LightModifierList
import com.intellij.psi.impl.source.tree.java.PsiCodeBlockImpl
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.plugin.frege.FregeLanguage
import com.plugin.frege.psi.FregeLabel
import com.plugin.frege.psi.FregeLabels
import com.plugin.frege.psi.FregeSigma
import com.plugin.frege.psi.FregeSimpleType
import com.plugin.frege.psi.impl.FregePsiMethodImpl
import com.plugin.frege.stubs.FregeMethodStub

abstract class FregeLabelMixin : FregePsiMethodImpl, FregeLabel {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FregeMethodStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun onlyQualifiedSearch(): Boolean {
        return true
    }

    override fun generateDoc(): String {
        return "" // TODO
    }

    override fun getParamsNumber(): Int {
        return PsiTreeUtil.getNextSiblingOfType(parentOfType<FregeLabels>(), FregeSigma::class.java)
            ?.children?.count { it is FregeSimpleType } ?: 0 // TODO waiting for type system
    }

    override fun getNameIdentifier(): PsiIdentifier? {
        return labelName
    }

    override fun getModifierList(): PsiModifierList {
        return LightModifierList(manager, FregeLanguage.INSTANCE, PsiModifier.PUBLIC, PsiModifier.FINAL)
    }

    override fun getBody(): PsiCodeBlock? {
        return PsiCodeBlockImpl(text)
    }

    override fun isConstructor(): Boolean {
        return false
    }
}
