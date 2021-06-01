package com.plugin.frege.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import com.intellij.psi.stubs.IStubElementType
import com.intellij.util.containers.addIfNotNull
import com.plugin.frege.psi.FregeDecl
import com.plugin.frege.psi.FregeNewtypeDecl
import com.plugin.frege.psi.FregePsiMethod
import com.plugin.frege.psi.impl.FregePsiClassImpl
import com.plugin.frege.stubs.FregeClassStub

abstract class FregeNewtypeDeclMixin : FregePsiClassImpl, FregeNewtypeDecl {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FregeClassStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameWithoutStub(): String {
        return nameIdentifier?.text ?: ""
    }

    override fun setName(name: String): PsiElement {
        listOf(1, 2, 3).asSequence()
        return this
    }

    override fun getNameIdentifier(): PsiIdentifier? {
        return conidUsage
    }

    override fun getMethods(): Array<PsiMethod> {
        val methods: MutableList<FregePsiMethod> = whereSection?.linearIndentSection?.subprogramsFromScope
            ?.mapNotNull { (it as? FregeDecl)?.binding }
            ?.toMutableList() ?: mutableListOf()
        methods.addIfNotNull(construct)
        return methods.toTypedArray()
    }

    override fun isInterface(): Boolean {
        return false
    }

    override fun getScope(): PsiElement {
        return this
    }
}