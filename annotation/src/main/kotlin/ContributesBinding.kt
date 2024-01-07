import dagger.hilt.GeneratesRootInput
import kotlin.reflect.KClass


@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.TYPE)
@GeneratesRootInput
annotation class ContributesBinding(val component: KClass<*>, val boundType: KClass<*> = Any::class)
