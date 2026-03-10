package railway.solver

import railway.model.RailwayNetwork
import java.util.LinkedList

object CargoSolver {
    fun solve(network: RailwayNetwork): Map<Int, Set<Int>> {
        val arrivingSets = network.stations.keys.associateWith { mutableSetOf<Int>() }.toMutableMap()

        val startStation = network.stations[network.startStation] ?: return arrivingSets

        val startDeparting = setOf(startStation.loadCargo)

        val worklist = LinkedList<Pair<Int, Set<Int>>>()
        for (neighbor in network.neighbors(network.startStation)) {
            worklist.add(neighbor to startDeparting)
        }

        while (worklist.isNotEmpty()) {
            val (stationId, incomingCargo) = worklist.poll()
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
