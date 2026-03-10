package railway.io

import railway.model.RailwayNetwork
import railway.model.Station
import railway.model.Track
import java.io.StreamTokenizer
import java.io.StringReader

object InputParser {
    fun parse(input: String): RailwayNetwork {
        val tokens = tokenize(input)

        val stationCount = tokens.next()
        val trackCount = tokens.next()

        val stations =
            buildMap {
                repeat(stationCount) {
                    val id = tokens.next()
                    require(id !in this) { "Duplicate station ID: $id" }
                    put(id, Station(id = id, unloadCargo = tokens.next(), loadCargo = tokens.next()))
                }
            }

        val tracks = List(trackCount) { Track(from = tokens.next(), to = tokens.next()) }

        val adjacency =
            tracks
                .groupBy { it.from }
                .mapValues { (_, v) -> v.map { it.to } }

        return RailwayNetwork(
            stations = stations,
            adjacency = adjacency,
            startStation = tokens.next(),
        )
    }

    private fun tokenize(input: String): Iterator<Int> {
        val st = StreamTokenizer(StringReader(input))
        return iterator {
            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                require(st.ttype == StreamTokenizer.TT_NUMBER) { "Expected integer, got: '${st.sval}'" }
                yield(st.nval.toInt())
            }
        }
    }
}
