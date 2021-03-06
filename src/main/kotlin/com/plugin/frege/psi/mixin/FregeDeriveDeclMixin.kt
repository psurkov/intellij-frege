package com.plugin.frege.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import com.plugin.frege.psi.FregeDeriveDecl
import com.plugin.frege.psi.FregeTypeParametersHolder
import com.plugin.frege.psi.FregeTypedVarid
import com.plugin.frege.psi.impl.FregeCompositeElementImpl

abstract class FregeDeriveDeclMixin(node: ASTNode) : FregeCompositeElementImpl(node), FregeTypeParametersHolder, FregeDeriveDecl {
    override val typedVaridDeclarations: List<FregeTypedVarid>
        get() = PsiTreeUtil.findChildrenOfType(typeApplications, FregeTypedVarid::class.java)
            .sortedBy { it.textOffset }
            .distinctBy { it.text }
}
