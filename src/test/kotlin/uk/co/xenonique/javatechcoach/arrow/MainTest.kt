package uk.co.xenonique.javatechcoach.arrow

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MainTest : FunSpec({
    test("my first test") {
        1 + 2 shouldBe 3
    }

    test(name ="a second test") {
        3 * 4 shouldBe 12
    }
})
