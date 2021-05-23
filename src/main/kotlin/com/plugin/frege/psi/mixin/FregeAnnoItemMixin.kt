package com.plugin.frege.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.*
import com.intellij.psi.impl.light.LightModifierList
import com.intellij.psi.impl.source.tree.java.PsiCodeBlockImpl
import com.intellij.psi.stubs.IStubElementType
import com.plugin.frege.FregeLanguage
import com.plugin.frege.psi.FregeAnnoItem
import com.plugin.frege.psi.FregeAnnotation
import com.plugin.frege.psi.FregeSimpleType
import com.plugin.frege.psi.impl.FregePsiMethodImpl
import com.plugin.frege.stubs.FregeMethodStub

abstract class FregeAnnoItemMixin : FregePsiMethodImpl, FregeAnnoItem {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FregeMethodStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getParamsNumber(): Int {
        val annotation = parent as? FregeAnnotation
        return annotation?.sigma?.children?.count { it is FregeSimpleType } ?: 0 // TODO it's VERY BAD. Waiting for grammar update.
    }

    override fun setName(name: String): PsiElement {
        return this // handleElementRename in AnnotationName performs renaming
    }

    override fun getModifierList(): PsiModifierList {
        return LightModifierList(manager, FregeLanguage.INSTANCE, PsiModifier.PUBLIC) // TODO
    }

    override fun getNameIdentifier(): PsiIdentifier? {
        return annotationName ?: symOp
    }

    override fun getBody(): PsiCodeBlock? {
        return PsiCodeBlockImpl(text)
    }

    override fun isConstructor(): Boolean {
        return false
    }

    // isn't required now but maybe later it will be
//    fun getBinding(): FregeBinding? {
//        val referenceText = name
//        return FregePsiUtilImpl.findElementsWithinScope(this) { elem ->
//            elem is FregeBinding && elem.name == referenceText
//        }.minByOrNull { it.textOffset } as? FregeBinding
//    }
}