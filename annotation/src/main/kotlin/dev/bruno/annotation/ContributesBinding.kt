package dev.bruno.annotation

import dagger.hilt.GeneratesRootInput
import kotlin.reflect.KClass


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@GeneratesRootInput
annotation class ContributesBinding(val component: KClass<*>, val boundType: KClass<*> = Any::class)
