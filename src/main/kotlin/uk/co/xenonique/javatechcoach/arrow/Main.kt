package uk.co.xenonique.javatechcoach.arrow

fun main() {
    println("=== Arrow.kt Webinar: From Java to Functional Kotlin ===\n")

    // Run the complete error handling evolution demo
    ErrorHandlingDemo.runDemo()

    // Then show the specific Either features
    EitherDemo.runDemo()

    // Show composition examples
    CompositionDemo.runDemo()

    // Add a dedicated formatting demo
    demonstrateFormatting()

    // Finish with advanced composition examples
    AdvancedCompositionDemo.runDemo()

    // Ior Demo
    IorDemo.runDemo()

    println("\n=== Webinar Complete ===")
    println("\nReady to level up your error handling? Let's talk!")
}



fun demonstrateFormatting() {
    println("\n--- Formatting & Final Presentation ---")

    val results = listOf(
        AdvancedCompositionDemo.processAndFormat("16"),
        AdvancedCompositionDemo.processAndFormat("abc"),
        AdvancedCompositionDemo.processAndFormat("-4"),
        AdvancedCompositionDemo.processAndFormat("25")
    )

    println("Final formatted results:")
    results.forEach { println("  • $it") }

    // Show how clean the business logic becomes
    println("""\n|🎯 Key Takeaway:
    |With Arrow.kt, your business logic stays clean and focused:
    |
    |fun businessLogic(input: String): Either<Error, Result> = either {
    |    val a = step1(input).bind()
    |    val b = step2(a).bind()
    |    step3(b).bind()
    |}
    |
    |fun presentResult(result: Either<Error, Result>): String =
    |    result.fold(
    |        ifLeft = { showError(it) },
    |        ifRight = { showSuccess(it) }
    |    )
    |
    |Separation of concerns: Business logic ≠ Presentation logic
    """.trimMargin())
}
