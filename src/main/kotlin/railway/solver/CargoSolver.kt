package railway.solver

import railway.model.CargoType
import railway.model.RailwayNetwork
import railway.model.StationId

object CargoSolver {
    fun solve(network: RailwayNetwork): Map<StationId, Set<CargoType>> {
        val arrivingSets =
            network.stations.keys
                .associateWith { mutableSetOf<CargoType>() }
                .toMutableMap()

        val startStation = network.stations[network.startStation] ?: return arrivingSets

        val worklist = ArrayDeque<Pair<StationId, Set<CargoType>>>()
        for (neighbor in network.neighbors(network.startStation)) {
            worklist.add(neighbor to setOf(startStation.loadCargo))
        }

        while (worklist.isNotEmpty()) {
            val (stationId, incomingCargo) = worklist.removeFirst()
            val arriving = arrivingSets.getValue(stationId)
            val newCargo = incomingCargo - arriving

            if (newCargo.isNotEmpty()) {
                arriving.addAll(newCargo)

                val station = network.stations.getValue(stationId)
                val departing = (arriving - station.unloadCargo) + station.loadCargo

                for (neighbor in network.neighbors(stationId)) {
                    worklist.add(neighbor to departing)
                }
            }
        }

        return arrivingSets.mapValues { (_, v) -> v.toSet() }
    }
}
