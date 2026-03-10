package railway.solver

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import railway.model.RailwayNetwork
import railway.model.Station

class CargoSolverTest : FunSpec({
    test("single station with no outgoing tracks") {
        val network = RailwayNetwork(
            stations = mapOf(1 to Station(1, 0, 1)),
            adjacency = emptyMap(),
            startStation = 1
        )
        val result = CargoSolver.solve(network)
        result[1] shouldBe emptySet()
    }

    test("linear chain: start -> A -> B") {
        val network = RailwayNetwork(
            stations = mapOf(
                1 to Station(1, 0, 10),
                2 to Station(2, 10, 20),
                3 to Station(3, 20, 30)
            ),
            adjacency = mapOf(1 to listOf(2), 2 to listOf(3)),
            startStation = 1
        )
        val result = CargoSolver.solve(network)
        result[1] shouldBe emptySet()
        result[2] shouldBe setOf(10)
        result[3] shouldBe setOf(20)
    }

    test("branching: start -> A and start -> B") {
        val network = RailwayNetwork(
            stations = mapOf(
                1 to Station(1, 0, 10),
                2 to Station(2, 10, 20),
                3 to Station(3, 10, 30)
            ),
            adjacency = mapOf(1 to listOf(2, 3)),
            startStation = 1
        )
        val result = CargoSolver.solve(network)
        result[2] shouldBe setOf(10)
        result[3] shouldBe setOf(10)
    }

    test("cycle accumulates cargo") {
        val network = RailwayNetwork(
            stations = mapOf(
                1 to Station(1, 0, 10),
                2 to Station(2, 0, 20),
                3 to Station(3, 0, 30)
            ),
            adjacency = mapOf(1 to listOf(2), 2 to listOf(3), 3 to listOf(2)),
            startStation = 1
        )
        val result = CargoSolver.solve(network)
        result[1] shouldBe emptySet()
        result[2] shouldBe setOf(10, 20, 30)
        result[3] shouldBe setOf(10, 20, 30)
    }

    test("unloading removes cargo type from arriving set") {
        val network = RailwayNetwork(
            stations = mapOf(
                1 to Station(1, 0, 10),
                2 to Station(2, 10, 20),
                3 to Station(3, 99, 30)
            ),
            adjacency = mapOf(1 to listOf(2), 2 to listOf(3)),
            startStation = 1
        )
        val result = CargoSolver.solve(network)
        result[3] shouldBe setOf(20)
    }

    test("station loads and unloads same cargo type") {
        val network = RailwayNetwork(
            stations = mapOf(
                1 to Station(1, 0, 10),
                2 to Station(2, 10, 10)
            ),
            adjacency = mapOf(1 to listOf(2)),
            startStation = 1
        )
        val result = CargoSolver.solve(network)
        result[2] shouldBe setOf(10)
    }

    test("unreachable station has empty cargo set") {
        val network = RailwayNetwork(
            stations = mapOf(
                1 to Station(1, 0, 10),
                2 to Station(2, 0, 20),
                3 to Station(3, 0, 30)
            ),
            adjacency = mapOf(1 to listOf(2)),
            startStation = 1
        )
        val result = CargoSolver.solve(network)
        result[3] shouldBe emptySet()
    }

    test("self-loop accumulates own cargo") {
        val network = RailwayNetwork(
            stations = mapOf(
                1 to Station(1, 0, 10),
                2 to Station(2, 0, 20)
            ),
            adjacency = mapOf(1 to listOf(2), 2 to listOf(2)),
            startStation = 1
        )
        val result = CargoSolver.solve(network)
        result[2] shouldBe setOf(10, 20)
    }

    test("duplicate tracks do not affect result") {
        val network = RailwayNetwork(
            stations = mapOf(
                1 to Station(1, 0, 10),
                2 to Station(2, 0, 20)
            ),
            adjacency = mapOf(1 to listOf(2, 2)),
            startStation = 1
        )
        val result = CargoSolver.solve(network)
        result[2] shouldBe setOf(10)
    }
})
