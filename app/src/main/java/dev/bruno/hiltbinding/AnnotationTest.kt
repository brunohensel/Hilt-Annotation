package dev.bruno.hiltbinding

import dagger.hilt.android.components.ActivityComponent
import dev.bruno.annotation.ContributesBinding
import javax.inject.Inject

@ContributesBinding(component = ActivityComponent::class)
class AnnotationTest @Inject constructor() : Test {
    override fun print(message: String) {
        println(message)
    }
}

interface Test {
    fun print(message: String)
}

//@Module
//@InstallIn(ActivityComponent::class)
//interface TestModule {
//
//    @Binds
//    fun bindTest(impl: AnnotationTest): Test
//}