package railway.model

data class RailwayNetwork(
    val stations: Map<StationId, Station>,
    val adjacency: Map<StationId, List<StationId>>,
    val startStation: StationId,
) {
    fun neighbors(stationId: StationId): List<StationId> = adjacency[stationId].orEmpty()
}
