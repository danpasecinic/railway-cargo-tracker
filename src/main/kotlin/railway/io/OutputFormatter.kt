package railway.io

import railway.model.CargoType
import railway.model.StationId

object OutputFormatter {
    fun format(cargoState: Map<StationId, Set<CargoType>>): String =
        cargoState
            .toSortedMap()
            .entries
            .joinToString("\n") { (stationId, cargo) ->
                val sortedCargo = cargo.sorted().joinToString(", ")
                "Station $stationId: [$sortedCargo]"
            }
}
