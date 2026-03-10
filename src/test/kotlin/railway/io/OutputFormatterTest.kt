package railway.io

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class OutputFormatterTest :
    FunSpec({
        test("formats cargo state sorted by station ID with sorted cargo") {
            val state =
                mapOf(
                    2 to setOf(30, 10),
                    1 to setOf(20, 5),
                    3 to emptySet(),
                )
            val result = OutputFormatter.format(state)
            result shouldBe
                """
                Station 1: [5, 20]
                Station 2: [10, 30]
                Station 3: []
                """.trimIndent()
        }

        test("formats empty state") {
            val state = emptyMap<Int, Set<Int>>()
            val result = OutputFormatter.format(state)
            result shouldBe ""
        }

        test("formats single station with no cargo") {
            val state = mapOf(1 to emptySet<Int>())
            val result = OutputFormatter.format(state)
            result shouldBe "Station 1: []"
        }
    })
