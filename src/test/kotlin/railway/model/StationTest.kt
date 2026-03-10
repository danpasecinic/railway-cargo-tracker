package railway.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StationTest : FunSpec({
    test("station holds id, unloadCargo, and loadCargo") {
        val station = Station(id = 1, unloadCargo = 2, loadCargo = 3)
        station.id shouldBe 1
        station.unloadCargo shouldBe 2
        station.loadCargo shouldBe 3
    }

    test("stations with same values are equal") {
        val a = Station(id = 1, unloadCargo = 2, loadCargo = 3)
        val b = Station(id = 1, unloadCargo = 2, loadCargo = 3)
        a shouldBe b
    }
})
