package railway.validation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import railway.model.RailwayNetwork
import railway.model.Station

class InputValidatorTest :
    FunSpec({
        test("valid network returns no errors") {
            val network =
                RailwayNetwork(
                    stations = mapOf(1 to Station(1, 0, 1), 2 to Station(2, 1, 2)),
                    adjacency = mapOf(1 to listOf(2)),
                    startStation = 1,
                )
            InputValidator.validate(network).shouldBeEmpty()
        }

        test("reports error when start station does not exist") {
            val network =
                RailwayNetwork(
                    stations = mapOf(1 to Station(1, 0, 1)),
                    adjacency = emptyMap(),
                    startStation = 99,
                )
            val errors = InputValidator.validate(network)
            errors shouldContain "Start station 99 does not exist"
        }

        test("reports error when track references undefined station") {
            val network =
                RailwayNetwork(
                    stations = mapOf(1 to Station(1, 0, 1)),
                    adjacency = mapOf(1 to listOf(99)),
                    startStation = 1,
                )
            val errors = InputValidator.validate(network)
            errors shouldContain "Station 99 referenced in track but not defined"
        }

        test("reports error for negative cargo type") {
            val network =
                RailwayNetwork(
                    stations = mapOf(1 to Station(1, -1, 2)),
                    adjacency = emptyMap(),
                    startStation = 1,
                )
            val errors = InputValidator.validate(network)
            errors shouldContain "Station 1 has negative cargo type: unloadCargo=-1"
        }
    })
