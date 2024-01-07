package dev.bruno

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class ContributeBindingVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val annotation = getAnnotation(classDeclaration) ?: return
        val component = annotation.arguments.component1().value!!
        val boundType = annotation.arguments.component2()
        val defaultArgument = annotation.defaultArguments.component1()
        val superTypesCount = classDeclaration.superTypes.count()
        val firstSuperType = classDeclaration.getAllSuperTypes().first()

        if (superTypesCount > 1 && boundType == defaultArgument) {
            logger.exception(
                IllegalArgumentException(
                    "There are more than one super type declared without any bounded type declaration "
                )
            )
        }

        val boundTypeArg = if (superTypesCount > 1) {
            BoundType(typeArg = boundType.value, typeName = null)
        } else BoundType(typeName = firstSuperType.toTypeName(), typeArg = null)

        val fileSpec: FileSpec = bindingFileSpec(
            subtype = classDeclaration.asType(listOf()),
            componentArg = component,
            boundTypeArg = boundTypeArg,
            logger = logger,
        ) ?: return

        fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
    }

    private fun getAnnotation(classDeclaration: KSClassDeclaration): KSAnnotation? {
        var ksAnnotation: KSAnnotation? = null
        for (annotation in classDeclaration.annotations) {
            if (annotation.annotationType.resolve().toClassName() == Utils.CONTRIBUTES_BINDING) {
                ksAnnotation = annotation
                break
            }
            ksAnnotation = null
        }
        return ksAnnotation
    }
}
