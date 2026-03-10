package railway.validation

import railway.model.RailwayNetwork

object InputValidator {
    fun validate(network: RailwayNetwork): List<String> = buildList {
        if (network.startStation !in network.stations) {
            add("Start station ${network.startStation} does not exist")
        }

        val referencedStations = network.adjacency.flatMap { (from, toList) -> toList + from }.toSet()

        referencedStations
            .filter { it !in network.stations }
            .forEach { add("Station $it referenced in track but not defined") }

        for (station in network.stations.values) {
            if (station.unloadCargo < 0) {
                add("Station ${station.id} has negative cargo type: unloadCargo=${station.unloadCargo}")
            }
            if (station.loadCargo < 0) {
                add("Station ${station.id} has negative cargo type: loadCargo=${station.loadCargo}")
            }
        }
    }
}
