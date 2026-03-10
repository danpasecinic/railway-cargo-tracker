package railway.validation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import railway.model.CargoType
import railway.model.RailwayNetwork
import railway.model.Station
import railway.model.StationId

private fun sid(v: Int) = StationId(v)

private fun ct(v: Int) = CargoType(v)

class InputValidatorTest :
    FunSpec({
        test("valid network returns no errors") {
            val network =
                RailwayNetwork(
                    stations =
                        mapOf(
                            sid(1) to Station(sid(1), ct(0), ct(1)),
                            sid(2) to Station(sid(2), ct(1), ct(2)),
                        ),
                    adjacency = mapOf(sid(1) to listOf(sid(2))),
                    startStation = sid(1),
                )
            InputValidator.validate(network).shouldBeEmpty()
        }

        test("reports error when start station does not exist") {
            val network =
                RailwayNetwork(
                    stations = mapOf(sid(1) to Station(sid(1), ct(0), ct(1))),
                    adjacency = emptyMap(),
                    startStation = sid(99),
                )
            val errors = InputValidator.validate(network)
            errors shouldContain "Start station 99 does not exist"
        }

        test("reports error when track references undefined station") {
            val network =
                RailwayNetwork(
                    stations = mapOf(sid(1) to Station(sid(1), ct(0), ct(1))),
                    adjacency = mapOf(sid(1) to listOf(sid(99))),
                    startStation = sid(1),
                )
            val errors = InputValidator.validate(network)
            errors shouldContain "Station 99 referenced in track but not defined"
        }

        test("reports error for negative cargo type") {
            val network =
                RailwayNetwork(
                    stations = mapOf(sid(1) to Station(sid(1), ct(-1), ct(2))),
                    adjacency = emptyMap(),
                    startStation = sid(1),
                )
            val errors = InputValidator.validate(network)
            errors shouldContain "Station 1 has negative cargo type: unloadCargo=-1"
        }

        test("reports error for negative load cargo type") {
            val network =
                RailwayNetwork(
                    stations = mapOf(sid(1) to Station(sid(1), ct(0), ct(-5))),
                    adjacency = emptyMap(),
                    startStation = sid(1),
                )
            val errors = InputValidator.validate(network)
            errors shouldContain "Station 1 has negative cargo type: loadCargo=-5"
        }

        test("reports multiple errors at once") {
            val network =
                RailwayNetwork(
                    stations = mapOf(sid(1) to Station(sid(1), ct(-1), ct(-2))),
                    adjacency = mapOf(sid(1) to listOf(sid(99))),
                    startStation = sid(88),
                )
            val errors = InputValidator.validate(network)
            errors.size shouldBe 4
        }
    })
