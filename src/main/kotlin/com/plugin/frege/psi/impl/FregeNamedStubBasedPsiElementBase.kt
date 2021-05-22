package com.plugin.frege.psi.impl

import com.intellij.psi.stubs.StubElement
import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.plugin.frege.psi.FregeNamedElement
import com.intellij.psi.stubs.IStubElementType
import com.plugin.frege.FregeIcons
import javax.swing.Icon

abstract class FregeNamedStubBasedPsiElementBase<T : StubElement<*>> : StubBasedPsiElementBase<T>, FregeNamedElement {
    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    constructor(node: ASTNode) : super(node)

    override fun getNavigationElement(): PsiElement {
        return nameIdentifier ?: this
    }

    override fun getTextOffset(): Int {
        val nameIdentifier = nameIdentifier
        return if (nameIdentifier != null && nameIdentifier !== this) {
            nameIdentifier.textOffset
        } else {
            super.getTextOffset()
        }
    }

    override fun getIcon(flags: Int): Icon? {
        return FregeIcons.FILE
    }
}
