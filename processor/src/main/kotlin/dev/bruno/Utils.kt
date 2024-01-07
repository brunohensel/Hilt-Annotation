package dev.bruno

import com.squareup.kotlinpoet.ClassName

object Utils {
    const val HiltComponentPackage = "dagger.hilt.android.components"

    val DAGGER_BINDS = ClassName("dagger", "Binds")
    val CONTRIBUTES_BINDING = ClassName("dev.bruno.annotation", "ContributesBinding")
    val INSTALL_IN = ClassName("dagger.hilt", "InstallIn")
    val DAGGER_MODULE = ClassName("dagger", "Module")
    val ORIGINATING_ELEMENT = ClassName("dagger.hilt.codegen", "OriginatingElement")
}

enum class HiltComponent {
    ActivityComponent,
    FragmentComponent,
    ServiceComponent,
    ViewComponent,
    ViewModelComponent,
    ActivityRetainedComponent,
    ViewWithFragmentComponent,
}
