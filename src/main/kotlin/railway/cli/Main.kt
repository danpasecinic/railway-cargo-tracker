package railway.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.options.option
import railway.io.InputParser
import railway.io.OutputFormatter
import railway.solver.CargoSolver
import railway.validation.InputValidator
import java.io.File

class RailwayCargoTracker : CliktCommand(
    name = "railway-cargo-tracker"
) {
    private val inputPath by option("--input", help = "Input file path (default: stdin)")
    private val outputPath by option("--output", help = "Output file path (default: stdout)")

    override fun run() {
        val input = try {
            when (val path = inputPath) {
                null -> generateSequence(::readLine).joinToString("\n")
                else -> File(path).readText()
            }
        } catch (e: Exception) {
            throw CliktError("Cannot read input file: ${e.message}")
        }

        val network = try {
            InputParser.parse(input)
        } catch (e: Exception) {
            throw CliktError("Invalid input: ${e.message}")
        }

        val errors = InputValidator.validate(network)
        if (errors.isNotEmpty()) {
            throw CliktError(errors.joinToString("\n"))
        }

        val result = CargoSolver.solve(network)
        val output = OutputFormatter.format(result)

        try {
            when (val path = outputPath) {
                null -> echo(output)
                else -> File(path).writeText(output + "\n")
            }
        } catch (e: Exception) {
            throw CliktError("Cannot write output file: ${e.message}")
        }
    }
}
