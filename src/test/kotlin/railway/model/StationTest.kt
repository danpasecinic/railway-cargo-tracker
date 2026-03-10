package railway.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StationTest :
    FunSpec({
        test("station holds id, unloadCargo, and loadCargo") {
            val station = Station(id = StationId(1), unloadCargo = CargoType(2), loadCargo = CargoType(3))
            station.id shouldBe StationId(1)
            station.unloadCargo shouldBe CargoType(2)
            station.loadCargo shouldBe CargoType(3)
        }

        test("stations with same values are equal") {
            val a = Station(id = StationId(1), unloadCargo = CargoType(2), loadCargo = CargoType(3))
            val b = Station(id = StationId(1), unloadCargo = CargoType(2), loadCargo = CargoType(3))
            a shouldBe b
        }
    })
