package dev.bruno.hiltbinding

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

class AnnotationTest @Inject constructor() : Test {
    override fun print(message: String) {
        println(message)
    }
}

interface Test {
    fun print(message: String)
}

@Module
@InstallIn(ActivityComponent::class)
interface TestModule {

    @Binds
    fun bindTest(impl: AnnotationTest): Test
}