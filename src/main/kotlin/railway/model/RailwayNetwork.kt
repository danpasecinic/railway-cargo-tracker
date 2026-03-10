package railway.model

data class RailwayNetwork(
    val stations: Map<Int, Station>,
    val adjacency: Map<Int, List<Int>>,
    val startStation: Int
) {
    fun neighbors(stationId: Int): List<Int> = adjacency[stationId].orEmpty()
}
