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
        val stationCount = header[0].toRequiredInt("Station count")
        val trackCount = header[1].toRequiredInt("Track count")

        require(lines.size == 1 + stationCount + trackCount + 1) {
            "Expected ${1 + stationCount + trackCount + 1} lines, got ${lines.size}"
        }

        val stations = mutableMapOf<Int, Station>()
        for (i in 1..stationCount) {
            val parts = lines[i].trim().split(WHITESPACE)
            require(parts.size == 3) { "Station line $i must have 3 values (id, unload, load), got ${parts.size}" }
            val id = parts[0].toRequiredInt("Station ID")
            val unload = parts[1].toRequiredInt("Unload cargo")
            val load = parts[2].toRequiredInt("Load cargo")
            require(id !in stations) { "Duplicate station ID: $id" }
            stations[id] = Station(id = id, unloadCargo = unload, loadCargo = load)
        }

        val tracks = (1..trackCount).map { i ->
            val parts = lines[stationCount + i].trim().split(WHITESPACE)
            require(parts.size == 2) { "Track line must have 2 values (from, to), got ${parts.size}" }
            Track(from = parts[0].toRequiredInt("Track 'from'"), to = parts[1].toRequiredInt("Track 'to'"))
        }

        val adjacency = tracks
            .groupBy { it.from }
            .mapValues { (_, v) -> v.map { it.to } }

        val startLine = lines[stationCount + trackCount + 1].trim()
        val startStation = startLine.toRequiredInt("Start station")

        return RailwayNetwork(
            stations = stations.toMap(),
            adjacency = adjacency,
            startStation = startStation
        )
    }

    private fun String.toRequiredInt(label: String): Int =
        toIntOrNull() ?: throw IllegalArgumentException("$label must be an integer: '$this'")
}
