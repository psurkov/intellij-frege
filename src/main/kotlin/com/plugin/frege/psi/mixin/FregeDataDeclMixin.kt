package com.plugin.frege.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import com.intellij.psi.stubs.IStubElementType
import com.plugin.frege.psi.FregeDataDecl
import com.plugin.frege.psi.FregeTypedVarid
import com.plugin.frege.psi.impl.FregePsiClassImpl
import com.plugin.frege.stubs.FregeClassStub

@Suppress("UnstableApiUsage")
abstract class FregeDataDeclMixin : FregePsiClassImpl<FregeClassStub>, FregeDataDecl {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FregeClassStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameWithoutStub(): String {
        return nameIdentifier.text
    }

    override fun getNameIdentifier(): PsiIdentifier {
        return conidUsage
    }

    override fun getMethods(): Array<PsiMethod> {
        return constructs.constructList.toTypedArray()
    }

    override fun isInterface(): Boolean {
        return false
    }

    override fun getScope(): PsiElement {
        return this
    }

    override val typedVaridDeclarations: List<FregeTypedVarid>
        get() = typedVaridList

    override fun generateDoc(): String {
        return generateDoc("Data", "Constructors")
    }
}
