package railway.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TrackTest :
    FunSpec({
        test("track holds from and to station IDs") {
            val track = Track(from = StationId(1), to = StationId(2))
            track.from shouldBe StationId(1)
            track.to shouldBe StationId(2)
        }
    })
