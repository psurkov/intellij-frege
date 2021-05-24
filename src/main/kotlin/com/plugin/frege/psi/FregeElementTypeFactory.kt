package com.plugin.frege.psi

import com.intellij.psi.tree.IElementType
import com.plugin.frege.stubs.types.*

object FregeElementTypeFactory {
    @JvmStatic
    fun factory(name: String): IElementType {
        return when (name) {
            "NATIVE_DATA_DECL" -> FregeNativeDataDeclElementType(name)
            "PROGRAM" -> FregeProgramElementType(name)
            "CLASS_DECL" -> FregeClassDeclElementType(name)
            "BINDING" -> FregeBindingElementType(name)
            "ANNOTATION_ITEM" -> FregeAnnotationItemElementType(name)
            else -> throw IllegalStateException("Unknown element name: $name")
        }
    }
}
