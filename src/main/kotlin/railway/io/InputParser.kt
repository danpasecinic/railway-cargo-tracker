package railway.io

import railway.model.RailwayNetwork
import railway.model.Station
import railway.model.Track

object InputParser {
    private val WHITESPACE = "\\s+".toRegex()

    fun parse(input: String): RailwayNetwork {
        val lines = input.lines().filter { it.isNotBlank() }
        require(lines.isNotEmpty()) { "Input is empty" }

        val header = lines[0].trim().split(WHITESPACE)
        require(header.size == 2) { "Header must contain exactly 2 values (S T), got ${header.size}" }
        val stationCount = header[0].toIntOrNull() ?: throw IllegalArgumentException("Station count must be an integer: '${header[0]}'")
        val trackCount = header[1].toIntOrNull() ?: throw IllegalArgumentException("Track count must be an integer: '${header[1]}'")

        val expectedLineCount = 1 + stationCount + trackCount + 1
        require(lines.size == expectedLineCount) {
            "Expected $expectedLineCount lines, got ${lines.size}"
        }

        val stations = mutableMapOf<Int, Station>()
        for (i in 1..stationCount) {
            val parts = lines[i].trim().split(WHITESPACE)
            require(parts.size == 3) { "Station line ${i} must have 3 values (id, unload, load), got ${parts.size}" }
            val id = parts[0].toIntOrNull() ?: throw IllegalArgumentException("Station ID must be an integer: '${parts[0]}'")
            val unload = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Unload cargo must be an integer: '${parts[1]}'")
            val load = parts[2].toIntOrNull() ?: throw IllegalArgumentException("Load cargo must be an integer: '${parts[2]}'")
            require(id !in stations) { "Duplicate station ID: $id" }
            stations[id] = Station(id = id, unloadCargo = unload, loadCargo = load)
        }

        val tracks = (1..trackCount).map { i ->
            val parts = lines[stationCount + i].trim().split(WHITESPACE)
            require(parts.size == 2) { "Track line must have 2 values (from, to), got ${parts.size}" }
            val from = parts[0].toIntOrNull() ?: throw IllegalArgumentException("Track 'from' must be an integer: '${parts[0]}'")
            val to = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Track 'to' must be an integer: '${parts[1]}'")
            Track(from = from, to = to)
        }

        val adjacency = tracks
            .groupBy { it.from }
            .mapValues { (_, tracks) -> tracks.map { it.to } }

        val startStation = lines[stationCount + trackCount + 1].trim().toIntOrNull()
            ?: throw IllegalArgumentException("Start station must be an integer: '${lines[stationCount + trackCount + 1].trim()}'")

        return RailwayNetwork(
            stations = stations.toMap(),
            adjacency = adjacency,
            startStation = startStation
        )
    }
}
