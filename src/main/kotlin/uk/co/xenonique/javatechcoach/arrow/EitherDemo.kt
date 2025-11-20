package uk.co.xenonique.javatechcoach.arrow


import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import kotlin.math.sqrt

object EitherDemo {

    // The problematic Java-style version
    fun sqrtFromStringJavaStyle(str: String): Double {
        val number = str.toInt() // Throws NumberFormatException!
        return sqrt(number.toDouble()) // Fails for negative numbers!
    }

    // The Arrow.kt solution
    fun sqrtFromString(str: String): Either<String, Double> = either {
        val number = str.toIntOrNull() ?: raise("'$str' is not a valid number")
        ensure(number >= 0) { "Cannot calculate square root of negative number: $number" }
        sqrt(number.toDouble())
    }

    fun runDemo() {
        println("\n--- Either Demo ---")

        // Test cases
        val testCases = listOf("4", "abc", "-4", "16")

        testCases.forEach { input ->
            val result = sqrtFromString(input)
            println("Input: '$input' -> Result: $result")

            // Show how to work with the result
            result.fold(
                ifLeft = { error -> println("  ❌ Error: $error") },
                ifRight = { value -> println("  ✅ Success: $value") }
            )
        }
    }
}