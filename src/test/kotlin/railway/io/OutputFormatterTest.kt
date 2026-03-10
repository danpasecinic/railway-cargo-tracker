package railway.io

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import railway.model.CargoType
import railway.model.StationId

private fun sid(v: Int) = StationId(v)

private fun ct(v: Int) = CargoType(v)

class OutputFormatterTest :
    FunSpec({
        test("formats cargo state sorted by station ID with sorted cargo") {
            val state =
                mapOf(
                    sid(2) to setOf(ct(30), ct(10)),
                    sid(1) to setOf(ct(20), ct(5)),
                    sid(3) to emptySet(),
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
            val state = emptyMap<StationId, Set<CargoType>>()
            val result = OutputFormatter.format(state)
            result shouldBe ""
        }

        test("formats single station with no cargo") {
            val state = mapOf(sid(1) to emptySet<CargoType>())
            val result = OutputFormatter.format(state)
            result shouldBe "Station 1: []"
        }

        test("formats single station with multiple cargo types sorted") {
            val state = mapOf(sid(1) to setOf(ct(30), ct(5), ct(20), ct(10)))
            val result = OutputFormatter.format(state)
            result shouldBe "Station 1: [5, 10, 20, 30]"
        }
    })
