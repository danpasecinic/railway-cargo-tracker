package railway.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.maps.shouldContainKey

class RailwayNetworkTest : FunSpec({
    test("network holds stations, adjacency, and start station") {
        val stations = mapOf(
            1 to Station(1, 0, 1),
            2 to Station(2, 1, 2)
        )
        val adjacency = mapOf(
            1 to listOf(2)
        )
        val network = RailwayNetwork(
            stations = stations,
            adjacency = adjacency,
            startStation = 1
        )
        network.stations shouldContainKey 1
        network.stations shouldContainKey 2
        network.adjacency[1] shouldBe listOf(2)
        network.startStation shouldBe 1
    }

    test("adjacency defaults to empty list for stations with no outgoing tracks") {
        val stations = mapOf(
            1 to Station(1, 0, 1),
            2 to Station(2, 1, 2)
        )
        val adjacency = mapOf(
            1 to listOf(2)
        )
        val network = RailwayNetwork(
            stations = stations,
            adjacency = adjacency,
            startStation = 1
        )
        network.neighbors(2) shouldBe emptyList()
    }
})
