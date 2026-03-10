package railway.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TrackTest : FunSpec({
    test("track holds from and to station IDs") {
        val track = Track(from = 1, to = 2)
        track.from shouldBe 1
        track.to shouldBe 2
    }
})
