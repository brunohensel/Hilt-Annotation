package dev.bruno

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import dev.bruno.Utils.HiltComponentPackage

internal fun bindingFileSpec(
    subtype: KSType,
    componentArg: Any,
    boundTypeArg: BoundType,
    logger: KSPLogger,
): FileSpec? {
    val hiltComponent: ClassName = resolveComponentFromArg(componentArg, logger) ?: return null
    val subtypeClassName = subtype.toClassName().topLevelClassName()
    val moduleName = ClassName(
        packageName = subtypeClassName.packageName,
        "${subtypeClassName.simpleNames.joinToString("_")}_HiltBindingModule"
    )

    val (boundType, typeName) = boundTypeArg
    val bindName = boundType?.javaClass?.simpleName ?: when (typeName) {
        is ClassName -> typeName.simpleName
        else -> error("Bound type is not a class")
    }

    val hiltModuleSpec = TypeSpec.interfaceBuilder(moduleName)
        .addAnnotation(Utils.DAGGER_MODULE)
        .addAnnotation(
            AnnotationSpec.builder(Utils.INSTALL_IN)
                .addMember("%T::class", hiltComponent)
                .build()
        )
        .addAnnotation(
            AnnotationSpec.builder(Utils.ORIGINATING_ELEMENT)
                .addMember("topLevelClass = %T::class", subtypeClassName)
                .build()
        )
        .addModifiers(KModifier.PUBLIC)
        .addFunction(
            FunSpec.builder("bind$bindName")
                .addAnnotation(Utils.DAGGER_BINDS)
                .addModifiers(KModifier.PUBLIC, KModifier.ABSTRACT)
                .applyReturn(boundType, typeName)
                .addParameter("impl", subtype.toTypeName())
                .build()
        )
        .build()
    return FileSpec.builder(moduleName).addType(hiltModuleSpec).build()
}

private fun resolveComponentFromArg(componentArg: Any, logger: KSPLogger): ClassName? {
    val component = HiltComponent.entries.firstOrNull { it.name == componentArg.toString() }
    if (component == null) {
        logger.exception(
            IllegalArgumentException(
                """$componentArg in not a Hilt component.
             it must be one of the following ${HiltComponent.entries}
            """.trimIndent()
            )
        )
        return null
    }
    return ClassName(HiltComponentPackage, component.name)
}

private fun FunSpec.Builder.applyReturn(boundTypeArg: Any?, typeName: TypeName?): FunSpec.Builder {
    return when {
        boundTypeArg != null -> returns(boundTypeArg::class)
        typeName != null -> returns(typeName)
        else -> throw IllegalArgumentException("boundTypeArg or typeName must not be null")
    }
}

data class BoundType(val typeArg: Any?, val typeName: TypeName?)