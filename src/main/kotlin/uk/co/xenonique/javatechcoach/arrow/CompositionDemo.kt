package uk.co.xenonique.javatechcoach.arrow

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import kotlin.math.sqrt

object CompositionDemo {

    fun parseNumber(str: String): Either<String, Int> = either {
        str.toIntOrNull() ?: raise("'$str' is not a valid number")
    }

    fun validatePositive(number: Int): Either<String, Int> = either {
        if (number >= 0) number else raise("Number must be positive: $number")
    }

    fun calculateSqrt(number: Int): Either<String, Double> = either {
        sqrt(number.toDouble())
    }

    // Composed pipeline - no if/else, no try/catch!
    fun processNumber(str: String): Either<String, Double> = either {
        val num = parseNumber(str).bind()
        val positiveNum = validatePositive(num).bind()
        calculateSqrt(positiveNum).bind()
    }

    fun runDemo() {
        println("\n--- Composition Demo ---")

        val result = processNumber("16")
        println("Final result: $result")

        // Show the step-by-step composition
        println("\nStep-by-step composition:")
        val results = listOf(
            parseNumber("16"),
            parseNumber("16").flatMap { validatePositive(it) },
            parseNumber("16").flatMap { validatePositive(it) }.flatMap { calculateSqrt(it) }
        )

        results.forEachIndexed { index, result ->
            println("Step ${index + 1}: $result")
        }
    }
}