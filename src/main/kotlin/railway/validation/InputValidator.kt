package railway.validation

import railway.model.RailwayNetwork

object InputValidator {
    fun validate(network: RailwayNetwork): List<String> {
        val errors = mutableListOf<String>()

        if (network.startStation !in network.stations) {
            errors.add("Start station ${network.startStation} does not exist")
        }

        val allReferencedStations = network.adjacency.entries.flatMap { (from, toList) ->
            listOf(from) + toList
        }.toSet()

        allReferencedStations
            .filter { it !in network.stations }
            .forEach { errors.add("Station $it referenced in track but not defined") }

        network.stations.values.forEach { station ->
            if (station.unloadCargo < 0) {
                errors.add("Station ${station.id} has negative cargo type: unloadCargo=${station.unloadCargo}")
            }
            if (station.loadCargo < 0) {
                errors.add("Station ${station.id} has negative cargo type: loadCargo=${station.loadCargo}")
            }
        }

        return errors
    }
}
