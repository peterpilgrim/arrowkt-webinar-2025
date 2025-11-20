package uk.co.xenonique.javatechcoach.arrow

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import kotlin.math.sqrt

object AdvancedCompositionDemo {

    // Your existing functions
    fun parseNumber(str: String): Either<String, Int> = either {
        str.toIntOrNull() ?: raise("'$str' is not a valid number")
    }

    fun validatePositive(number: Int): Either<String, Int> = either {
        if (number >= 0) number else raise("Number must be positive: $number")
    }

    fun calculateSqrt(number: Int): Either<String, Double> = either {
        sqrt(number.toDouble())
    }

    // The formatting function
    fun formatResult(result: Either<String, Double>): String = result.fold(
        ifLeft = { error -> "❌ Error: $error" },
        ifRight = { value -> "✅ Result: ${"%.3f".format(value)}" }
    )

    // Composed pipeline that includes formatting
    fun processAndFormat(str: String): String = either {
        val num = parseNumber(str).bind()
        val positiveNum = validatePositive(num).bind()
        calculateSqrt(positiveNum).bind()
    }.let { formatResult(it) } // Format the final result

    fun runDemo() {
        println("\n--- Composition with Formatting Demo ---")

        val inputs = listOf("16", "abc", "-4", "25")

        inputs.forEach { input ->
            val result = processAndFormat(input)
            println("Input: '$input' -> $result")
        }

        // Show the step-by-step composition
        println("\n--- Step-by-step Composition ---")

        val step1 = parseNumber("16")
        println("Step 1 - Parse: $step1")

        val step2 = step1.flatMap { validatePositive(it) }
        println("Step 2 - Validate: $step2")

        val step3 = step2.flatMap { calculateSqrt(it) }
        println("Step 3 - Calculate: $step3")

        val final = formatResult(step3)
        println("Step 4 - Format: $final")

        // Demonstrate the power of map
        println("\n--- Using Map for Transformations ---")

        val transformed = calculateSqrt(16)
            .map { it * 100 } // Transform success value
            .map { "Percentage: ${"%.1f".format(it)}%" } // Transform to string
            .mapLeft { error -> "MATH ERROR: $error" } // Transform error message

        println("Transformed result: $transformed")
    }
}