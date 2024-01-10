package dev.bruno

import com.squareup.kotlinpoet.ClassName

object Utils {
    val DAGGER_BINDS = ClassName("dagger", "Binds")
    val CONTRIBUTES_BINDING = ClassName("dev.bruno.annotation", "ContributesBinding")
    val INSTALL_IN = ClassName("dagger.hilt", "InstallIn")
    val DAGGER_MODULE = ClassName("dagger", "Module")
    val ORIGINATING_ELEMENT = ClassName("dagger.hilt.codegen", "OriginatingElement")
}
