package railway.io

import railway.model.RailwayNetwork
import railway.model.Station
import railway.model.Track

object InputParser {
    fun parse(input: String): RailwayNetwork {
        val lines = input.lines().filter { it.isNotBlank() }
        val (stationCount, trackCount) = lines[0].trim().split("\\s+".toRegex()).map { it.toInt() }

        val expectedLineCount = 1 + stationCount + trackCount + 1
        require(lines.size == expectedLineCount) {
            "Expected $expectedLineCount lines, got ${lines.size}"
        }

        val stations = mutableMapOf<Int, Station>()
        for (i in 1..stationCount) {
            val parts = lines[i].trim().split("\\s+".toRegex()).map { it.toInt() }
            val id = parts[0]
            require(id !in stations) { "Duplicate station ID: $id" }
            stations[id] = Station(id = id, unloadCargo = parts[1], loadCargo = parts[2])
        }

        val tracks = (1..trackCount).map { i ->
            val parts = lines[stationCount + i].trim().split("\\s+".toRegex()).map { it.toInt() }
            Track(from = parts[0], to = parts[1])
        }

        val adjacency = tracks
            .groupBy { it.from }
            .mapValues { (_, tracks) -> tracks.map { it.to } }

        val startStation = lines[stationCount + trackCount + 1].trim().toInt()

        return RailwayNetwork(
            stations = stations.toMap(),
            adjacency = adjacency,
            startStation = startStation
        )
    }
}
