package railway.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.shouldBe

class RailwayNetworkTest :
    FunSpec({
        val network =
            RailwayNetwork(
                stations =
                    mapOf(
                        StationId(1) to Station(StationId(1), CargoType(0), CargoType(1)),
                        StationId(2) to Station(StationId(2), CargoType(1), CargoType(2)),
                    ),
                adjacency = mapOf(StationId(1) to listOf(StationId(2))),
                startStation = StationId(1),
            )

        test("network holds stations, adjacency, and start station") {
            network.stations shouldContainKey StationId(1)
            network.stations shouldContainKey StationId(2)
            network.adjacency[StationId(1)] shouldBe listOf(StationId(2))
            network.startStation shouldBe StationId(1)
        }

        test("adjacency defaults to empty list for stations with no outgoing tracks") {
            network.neighbors(StationId(2)) shouldBe emptyList()
        }
    })
