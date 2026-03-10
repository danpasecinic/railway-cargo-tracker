package railway.io

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import railway.model.CargoType
import railway.model.Station
import railway.model.StationId

private fun sid(v: Int) = StationId(v)

private fun ct(v: Int) = CargoType(v)

class InputParserTest :
    FunSpec({
        test("parses valid input into RailwayNetwork") {
            val input =
                """
                3 2
                1 0 1
                2 1 2
                3 2 3
                1 2
                2 3
                1
                """.trimIndent()

            val network = InputParser.parse(input)

            network.stations.size shouldBe 3
            network.stations[sid(1)] shouldBe Station(sid(1), ct(0), ct(1))
            network.stations[sid(2)] shouldBe Station(sid(2), ct(1), ct(2))
            network.stations[sid(3)] shouldBe Station(sid(3), ct(2), ct(3))
            network.adjacency[sid(1)] shouldBe listOf(sid(2))
            network.adjacency[sid(2)] shouldBe listOf(sid(3))
            network.startStation shouldBe sid(1)
        }

        test("parses input with multiple outgoing tracks from same station") {
            val input =
                """
                2 2
                1 0 1
                2 1 2
                1 2
                1 2
                1
                """.trimIndent()

            val network = InputParser.parse(input)
            network.adjacency[sid(1)] shouldBe listOf(sid(2), sid(2))
        }

        test("parses input with no tracks") {
            val input =
                """
                1 0
                1 0 1
                1
                """.trimIndent()

            val network = InputParser.parse(input)
            network.stations.size shouldBe 1
            network.adjacency shouldBe emptyMap()
            network.startStation shouldBe sid(1)
        }

        test("throws on station count mismatch") {
            val input =
                """
                3 0
                1 0 1
                2 1 2
                1
                """.trimIndent()

            shouldThrow<IllegalArgumentException> {
                InputParser.parse(input)
            }
        }

        test("throws on duplicate station ID") {
            val input =
                """
                2 0
                1 0 1
                1 2 3
                1
                """.trimIndent()

            shouldThrow<IllegalArgumentException> {
                InputParser.parse(input)
            }
        }

        test("throws on non-numeric input") {
            val input = "abc"

            shouldThrow<IllegalArgumentException> {
                InputParser.parse(input)
            }
        }

        test("parses zero stations and zero tracks") {
            val input =
                """
                0 0
                1
                """.trimIndent()

            val network = InputParser.parse(input)
            network.stations shouldBe emptyMap()
            network.adjacency shouldBe emptyMap()
            network.startStation shouldBe sid(1)
        }
    })
