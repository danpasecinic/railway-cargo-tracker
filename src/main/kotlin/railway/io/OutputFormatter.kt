package railway.io

object OutputFormatter {
    fun format(cargoState: Map<Int, Set<Int>>): String =
        cargoState
            .toSortedMap()
            .entries
            .joinToString("\n") { (stationId, cargo) ->
                val sortedCargo = cargo.sorted().joinToString(", ")
                "Station $stationId: [$sortedCargo]"
            }
}
