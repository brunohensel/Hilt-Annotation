import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import dev.bruno.ContributeBindingVisitor
import dev.bruno.Utils

class BindingProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Utils.CONTRIBUTES_BINDING.canonicalName)
        val (validSymbols, invalidSymbols) = symbols.partition { it.validate() }

        for (symbol in validSymbols) {
            if (symbol is KSClassDeclaration) {
                symbol.accept(
                    visitor = ContributeBindingVisitor(codeGenerator, logger),
                    data = Unit
                )
            }
        }
        return invalidSymbols
    }
}

class BindingProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return BindingProcessor(environment.codeGenerator, environment.logger)
    }
}