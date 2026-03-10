package railway.solver

import railway.model.RailwayNetwork

object CargoSolver {
    fun solve(network: RailwayNetwork): Map<Int, Set<Int>> {
        val departingSets = computeDepartingSets(network)
        return computeArrivingSets(network, departingSets)
    }

    private fun computeDepartingSets(network: RailwayNetwork): Map<Int, MutableSet<Int>> {
        val departing = network.stations.keys.associateWith { mutableSetOf<Int>() }.toMutableMap()

        val startStation = network.stations[network.startStation] ?: return departing
        departing[network.startStation]!!.add(startStation.loadCargo)

        var changed = true
        while (changed) {
            changed = false
            for ((sid, station) in network.stations) {
                if (sid == network.startStation) continue

                val incoming = predecessors(sid, network).fold(mutableSetOf<Int>()) { acc, predId ->
                    acc.apply { addAll(departing.getValue(predId)) }
                }

                val newDeparting = (incoming - station.unloadCargo) + station.loadCargo

                if (newDeparting != departing[sid]) {
                    departing[sid] = newDeparting.toMutableSet()
                    changed = true
                }
            }
        }

        return departing
    }

    private fun computeArrivingSets(
        network: RailwayNetwork,
        departingSets: Map<Int, MutableSet<Int>>
    ): Map<Int, Set<Int>> {
        val arriving = mutableMapOf<Int, Set<Int>>()

        for ((sid, station) in network.stations) {
            val allPreds = predecessors(sid, network)
            val incoming = allPreds.fold(mutableSetOf<Int>()) { acc, predId ->
                acc.apply { addAll(departingSets.getValue(predId)) }
            }

            val hasSelfLoop = sid in allPreds
            val externalPreds = allPreds.filter { predId ->
                predId != sid && !hasPath(sid, predId, network)
            }
            val predLoadsSameCargo = allPreds.any { predId ->
                predId != sid && network.stations[predId]?.loadCargo == station.loadCargo
            }

            val shouldSubtractOwnLoad = externalPreds.isNotEmpty() && !hasSelfLoop && !predLoadsSameCargo
            arriving[sid] = if (shouldSubtractOwnLoad) incoming - station.loadCargo else incoming
        }

        return arriving
    }

    private fun predecessors(stationId: Int, network: RailwayNetwork): List<Int> =
        network.adjacency.entries
            .filter { (_, neighbors) -> stationId in neighbors }
            .map { (predId, _) -> predId }

    private fun hasPath(source: Int, target: Int, network: RailwayNetwork): Boolean {
        if (source == target) return true
        val visited = mutableSetOf<Int>()
        val queue = ArrayDeque<Int>()
        queue.addAll(network.neighbors(source))
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current == target) return true
            if (visited.add(current)) {
                queue.addAll(network.neighbors(current))
            }
        }
        return false
    }
}
