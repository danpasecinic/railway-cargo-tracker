package railway.solver

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import railway.model.CargoType
import railway.model.RailwayNetwork
import railway.model.Station
import railway.model.StationId

private fun sid(v: Int) = StationId(v)

private fun ct(v: Int) = CargoType(v)

class CargoSolverTest :
    FunSpec({
        test("single station with no outgoing tracks") {
            val network =
                RailwayNetwork(
                    stations = mapOf(sid(1) to Station(sid(1), ct(0), ct(1))),
                    adjacency = emptyMap(),
                    startStation = sid(1),
                )
            val result = CargoSolver.solve(network)
            result[sid(1)] shouldBe emptySet()
        }

        test("linear chain: start -> A -> B") {
            val network =
                RailwayNetwork(
                    stations =
                        mapOf(
                            sid(1) to Station(sid(1), ct(0), ct(10)),
                            sid(2) to Station(sid(2), ct(10), ct(20)),
                            sid(3) to Station(sid(3), ct(20), ct(30)),
                        ),
                    adjacency = mapOf(sid(1) to listOf(sid(2)), sid(2) to listOf(sid(3))),
                    startStation = sid(1),
                )
            val result = CargoSolver.solve(network)
            result[sid(1)] shouldBe emptySet()
            result[sid(2)] shouldBe setOf(ct(10))
            result[sid(3)] shouldBe setOf(ct(20))
        }

        test("branching: start -> A and start -> B") {
            val network =
                RailwayNetwork(
                    stations =
                        mapOf(
                            sid(1) to Station(sid(1), ct(0), ct(10)),
                            sid(2) to Station(sid(2), ct(10), ct(20)),
                            sid(3) to Station(sid(3), ct(10), ct(30)),
                        ),
                    adjacency = mapOf(sid(1) to listOf(sid(2), sid(3))),
                    startStation = sid(1),
                )
            val result = CargoSolver.solve(network)
            result[sid(2)] shouldBe setOf(ct(10))
            result[sid(3)] shouldBe setOf(ct(10))
        }

        test("cycle accumulates cargo") {
            val network =
                RailwayNetwork(
                    stations =
                        mapOf(
                            sid(1) to Station(sid(1), ct(0), ct(10)),
                            sid(2) to Station(sid(2), ct(0), ct(20)),
                            sid(3) to Station(sid(3), ct(0), ct(30)),
                        ),
                    adjacency =
                        mapOf(
                            sid(1) to listOf(sid(2)),
                            sid(2) to listOf(sid(3)),
                            sid(3) to listOf(sid(2)),
                        ),
                    startStation = sid(1),
                )
            val result = CargoSolver.solve(network)
            result[sid(1)] shouldBe emptySet()
            result[sid(2)] shouldBe setOf(ct(10), ct(20), ct(30))
            result[sid(3)] shouldBe setOf(ct(10), ct(20), ct(30))
        }

        test("unloading removes cargo type from arriving set") {
            val network =
                RailwayNetwork(
                    stations =
                        mapOf(
                            sid(1) to Station(sid(1), ct(0), ct(10)),
                            sid(2) to Station(sid(2), ct(10), ct(20)),
                            sid(3) to Station(sid(3), ct(99), ct(30)),
                        ),
                    adjacency = mapOf(sid(1) to listOf(sid(2)), sid(2) to listOf(sid(3))),
                    startStation = sid(1),
                )
            val result = CargoSolver.solve(network)
            result[sid(3)] shouldBe setOf(ct(20))
        }

        test("station loads and unloads same cargo type") {
            val network =
                RailwayNetwork(
                    stations =
                        mapOf(
                            sid(1) to Station(sid(1), ct(0), ct(10)),
                            sid(2) to Station(sid(2), ct(10), ct(10)),
                        ),
                    adjacency = mapOf(sid(1) to listOf(sid(2))),
                    startStation = sid(1),
                )
            val result = CargoSolver.solve(network)
            result[sid(2)] shouldBe setOf(ct(10))
        }

        test("unreachable station has empty cargo set") {
            val network =
                RailwayNetwork(
                    stations =
                        mapOf(
                            sid(1) to Station(sid(1), ct(0), ct(10)),
                            sid(2) to Station(sid(2), ct(0), ct(20)),
                            sid(3) to Station(sid(3), ct(0), ct(30)),
                        ),
                    adjacency = mapOf(sid(1) to listOf(sid(2))),
                    startStation = sid(1),
                )
            val result = CargoSolver.solve(network)
            result[sid(3)] shouldBe emptySet()
        }

        test("self-loop accumulates own cargo") {
            val network =
                RailwayNetwork(
                    stations =
                        mapOf(
                            sid(1) to Station(sid(1), ct(0), ct(10)),
                            sid(2) to Station(sid(2), ct(0), ct(20)),
                        ),
                    adjacency = mapOf(sid(1) to listOf(sid(2)), sid(2) to listOf(sid(2))),
                    startStation = sid(1),
                )
            val result = CargoSolver.solve(network)
            result[sid(2)] shouldBe setOf(ct(10), ct(20))
        }

        test("duplicate tracks do not affect result") {
            val network =
                RailwayNetwork(
                    stations =
                        mapOf(
                            sid(1) to Station(sid(1), ct(0), ct(10)),
                            sid(2) to Station(sid(2), ct(0), ct(20)),
                        ),
                    adjacency = mapOf(sid(1) to listOf(sid(2), sid(2))),
                    startStation = sid(1),
                )
            val result = CargoSolver.solve(network)
            result[sid(2)] shouldBe setOf(ct(10))
        }
    })
