package com.plugin.frege.documentation

import com.intellij.psi.util.parentOfType
import com.plugin.frege.psi.*
import com.plugin.frege.psi.impl.FregeAnnotationItemImpl
import com.plugin.frege.psi.impl.FregeBindingImpl
import com.plugin.frege.psi.impl.FregeClassDeclImpl
import com.plugin.frege.psi.impl.FregePsiUtilImpl

object FregeGenerateDocUtil {
    @JvmStatic
    fun generateFregeMethodDoc(method: FregePsiMethod): String {
        val annoItem = when (method) {
            is FregeBindingImpl -> method.getAnnoItem() as? FregeAnnotationItemImpl
            is FregeAnnotationItemImpl -> method
            else -> null
        }
        return buildDoc {
            definition {
                appendModuleLink(method.parentOfType())
                appendNewline()
                appendText("Function ")
                appendBoldText(method.name)
                val type = annoItem?.getAnnotation()?.sigma?.text
                if (type != null) {
                    appendNewline()
                    appendCode("Type: $type")
                }
                val fregeClass = method.containingClass as? FregeClassDecl
                if (fregeClass != null) {
                    appendNewline()
                    appendText("within ")
                    appendPsiClassLink(fregeClass)
                }
            }
            content {
                appendDocs(annoItem)
                section("Implementations:") {
                    for (binding in FregePsiUtilImpl.getAllBindingsOfMethod(method)) {
                        paragraph {
                            appendDocs(binding)
                            appendNewline()
                            appendCode(binding.lhs.text)
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    private fun generateFregePsiClassDoc(
        fregePsiClass: FregePsiClass,
        psiClassTitle: String,
        psiMethodTitle: String
    ): String {
        val uniqueMethods = fregePsiClass.allMethods.distinctBy { it.name }.mapNotNull { it as? FregePsiMethod }
        val psiClassName = fregePsiClass.name
        return buildDoc {
            definition {
                appendModuleLink(fregePsiClass.parentOfType())
                if (psiClassName != null) {
                    appendNewline()
                    appendText("$psiClassTitle ")
                    appendBoldText(psiClassName)
                }
            }
            content {
                appendDocs(fregePsiClass)
                section("$psiMethodTitle:") {
                    for (method in uniqueMethods) {
                        paragraph { appendPsiMethodLink(method) }
                    }
                }
            }
        }
    }

    @JvmStatic
    fun generateFregeClassDoc(element: FregeClassDeclImpl): String {
        return generateFregePsiClassDoc(element, "Class", "Functions")
    }

    @JvmStatic
    fun generateFregeDataDoc(element: FregeDataDecl): String {
        return generateFregePsiClassDoc(element, "Data", "Constructors")
    }

    @JvmStatic
    fun generateFregeProgramDoc(fregeProgram: FregeProgram): String {
        val moduleName = fregeProgram.packageName?.text ?: return ""
        return buildDoc {
            definition {
                appendText("Module ")
                appendBoldText(moduleName)
            }
            content {
                appendDocs(fregeProgram)
            }
        }
    }
}
