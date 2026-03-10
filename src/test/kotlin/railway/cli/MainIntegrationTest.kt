package railway.cli

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.parse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File

class MainIntegrationTest :
    FunSpec({
        fun tempFile(content: String? = null): File =
            File.createTempFile("railway-test", ".txt").apply {
                deleteOnExit()
                if (content != null) writeText(content)
            }

        fun runTracker(
            inputPath: String,
            outputPath: String? = null,
        ) {
            val args = mutableListOf("--input", inputPath)
            if (outputPath != null) args += listOf("--output", outputPath)
            RailwayCargoTracker().parse(args.toTypedArray())
        }

        test("end-to-end: linear chain from file") {
            val inputFile =
                tempFile(
                    """
                    3 2
                    1 0 10
                    2 10 20
                    3 20 30
                    1 2
                    2 3
                    1
                    """.trimIndent(),
                )
            val outputFile = tempFile()

            runTracker(inputFile.absolutePath, outputFile.absolutePath)

            outputFile.readText().trim() shouldBe
                """
                Station 1: []
                Station 2: [10]
                Station 3: [20]
                """.trimIndent()
        }

        test("end-to-end: cycle with cargo accumulation from file") {
            val inputFile =
                tempFile(
                    """
                    3 3
                    1 0 10
                    2 0 20
                    3 0 30
                    1 2
                    2 3
                    3 2
                    1
                    """.trimIndent(),
                )
            val outputFile = tempFile()

            runTracker(inputFile.absolutePath, outputFile.absolutePath)

            outputFile.readText().trim() shouldBe
                """
                Station 1: []
                Station 2: [10, 20, 30]
                Station 3: [10, 20, 30]
                """.trimIndent()
        }

        test("validation error throws CliktError") {
            val inputFile =
                tempFile(
                    """
                    1 1
                    1 0 10
                    1 99
                    1
                    """.trimIndent(),
                )

            val error =
                shouldThrow<CliktError> {
                    runTracker(inputFile.absolutePath)
                }
            error.message shouldContain "referenced in track but not defined"
        }

        test("file not found throws CliktError") {
            val error =
                shouldThrow<CliktError> {
                    runTracker("/nonexistent/file.txt")
                }
            error.message shouldContain "Cannot read input file"
        }
    })
