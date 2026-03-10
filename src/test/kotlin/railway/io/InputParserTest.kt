package railway.io

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import railway.model.Station

class InputParserTest : FunSpec({
    test("parses valid input into RailwayNetwork") {
        val input = """
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
        network.stations[1] shouldBe Station(1, 0, 1)
        network.stations[2] shouldBe Station(2, 1, 2)
        network.stations[3] shouldBe Station(3, 2, 3)
        network.adjacency[1] shouldBe listOf(2)
        network.adjacency[2] shouldBe listOf(3)
        network.startStation shouldBe 1
    }

    test("parses input with multiple outgoing tracks from same station") {
        val input = """
            2 2
            1 0 1
            2 1 2
            1 2
            1 2
            1
        """.trimIndent()

        val network = InputParser.parse(input)
        network.adjacency[1] shouldBe listOf(2, 2)
    }

    test("parses input with no tracks") {
        val input = """
            1 0
            1 0 1
            1
        """.trimIndent()

        val network = InputParser.parse(input)
        network.stations.size shouldBe 1
        network.adjacency shouldBe emptyMap()
        network.startStation shouldBe 1
    }

    test("throws on station count mismatch") {
        val input = """
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
        val input = """
            2 0
            1 0 1
            1 2 3
            1
        """.trimIndent()

        shouldThrow<IllegalArgumentException> {
            InputParser.parse(input)
        }
    }
})
